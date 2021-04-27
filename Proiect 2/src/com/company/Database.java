package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Database {
    public @interface Audited { }
    protected Map<String, Class<? extends TableRow>> classes = new HashMap<>();
    protected Map<Class<? extends TableRow>, Map<Integer, TableRow>> maps = new HashMap<>();
    protected Map<Class<? extends TableRow>, Constructor<?>> constructors = new HashMap<>();
    ArrayList<String> priority = new ArrayList<>();

    /**
     * Adds a table to the database given the new flag and class.
     * @param flag the flag's name
     * @param cls the type of the "rows"
     * @implSpec The class must have exactly one constructor annotated with "@ID" which takes any number of String parameters.
     */
    @Audited public void addTable(String flag, Class<? extends TableRow> cls) {
        try {
            if (classes.containsKey(flag)) { throw new RuntimeException("Flag "+flag+" already used"); }
            Constructor<?> constructor = null;
            for (Constructor<?> ctor: cls.getConstructors()) {
                for (Annotation annotation: ctor.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(ID.class)) {
                        if (constructor != null) { throw new RuntimeException("Multiple constructors annotated with @ID"); }
                        constructor = ctor;
                    }
                }
            }
            if (constructor == null) { throw new RuntimeException("No constructor annotated with @ID"); }
            classes.put(flag, cls);
            maps.put(cls, new HashMap<>());
            constructors.put(cls, constructor);
            priority.add(flag);
            Audit.entry("Table added (flag: "+flag+", type: "+cls.getSimpleName()+")", AuditLevel.CREATE_TABLE);
        } catch (RuntimeException e) { System.err.println(e.getMessage()); }
    }

    /**
     * Deletes everything from the database.
     */
    @Audited public void clear() {
        maps.clear();
        classes.clear();
        constructors.clear();
        priority.clear();
        Audit.entry("All tables deleted", AuditLevel.DELETE_TABLE);
    }

    /**
     * Deletes a table from the database given its flag.
     * @param flag the name of the flag
     */
    @Audited public void deleteTable(String flag) {
        if (!classes.containsKey(flag)) { System.out.println("No table with flag "+flag); }
        else {
            for (int key: maps.get(classes.get(flag)).keySet()) { delete(classes.get(flag), key); }
            maps.remove(classes.get(flag));
            classes.remove(flag);
            constructors.remove(classes.get(flag));
            priority.remove(flag);
            Audit.entry("Table deleted (flag "+flag+")", AuditLevel.DELETE_TABLE);
        }
    }

    /**
     * Add an object to the database if all dependencies are satisfied.
     * @param row the object to add
     * @param <T> must extend TableRow
     */
    @Audited public <T extends TableRow> void add(T row) {
        if (!maps.containsKey(row.getClass())) {
            System.err.println("Class "+row.getClass().getSimpleName()+" is not in the database");
            return;
        }
        for (Class<?> dependency: row.hasDependency().keySet()) {
            if (!maps.containsKey(dependency)) {
                System.err.println("No table of "+dependency.getSimpleName()+"s found");
                return;
            }
            if (!maps.get(dependency).containsKey(row.hasDependency().get(dependency))) {
                System.err.println("Invalid "+row.getClass().getSimpleName()+": "+row+" (no "+dependency.getSimpleName()+" with id "+row.hasDependency().get(dependency)+")");
                return;
            }
        }
        for (Class<?> dependency: row.hasDependency().keySet()) {
            maps.get(dependency).get(row.hasDependency().get(dependency)).addDependency(row.getClass(), row.id());
        }
        maps.get(row.getClass()).put(row.id, row);
        Audit.entry("Added "+row.getClass().getSimpleName()+ " " +row.id(), AuditLevel.ADD_ROW);
    }

    /**
     * Deletes an object from the database given the class and id.
     * @param cls the class of the object (must extend TableRow)
     * @param id the object's id
     * @implNote The objects are never returned from the database and no copies can be made due to their unique id, so the only solution is to pass the class and id.
     */
    @Audited public void delete(Class<? extends TableRow> cls, Integer id) {
        if (!maps.get(cls).containsKey(id)) {
            System.err.println("No object found while trying to delete. Circular dependency?");
            return;
        }
        Map<Class<? extends TableRow>, Integer> hasDependency = new HashMap<>(maps.get(cls).get(id).hasDependency());
        for (Class<? extends TableRow> dependency: hasDependency.keySet()) {
            System.out.printf("%20s%15s%n", "("+cls.getSimpleName()+" "+id+"):  ", "\033[32mRemove\033[0m weak dependency from ("+dependency.getSimpleName()+" "+hasDependency.get(dependency)+")");
            maps.get(dependency).get(hasDependency.get(dependency)).removeDependency(cls, id);
        }
        for (Class<? extends TableRow> dependency: maps.get(cls).get(id).isDependency().keySet()) {
            ArrayList<Integer> nonConcurrent = new ArrayList<>(maps.get(cls).get(id).isDependency().get(dependency));
            for (int dependencyID: nonConcurrent) {
                System.out.printf("%20s%15s%n", "("+cls.getSimpleName()+" "+id+"):  ", "\033[31mDelete\033[0m strong dependency ("+dependency.getSimpleName()+" "+dependencyID+")");
                delete(dependency, dependencyID);
            }
        }
        maps.get(cls).remove(id);
        Audit.entry("Deleted "+cls.getSimpleName()+ " " +id, AuditLevel.DELETE_ROW);
    }

    /**
     * Instantiate a new object and add it to the corresponding map from the database.
     * @param cls the object's class (must extend TableRow)
     * @param parameters the parameters to be passed to constructor
     */
    public void instantiateObject(Class<? extends TableRow> cls, String[] parameters) {
        try {
            if (!maps.containsKey(cls)) { throw new RuntimeException("Class "+cls.getSimpleName()+" not in database."); }
            add((TableRow) constructors.get(cls).newInstance((Object[]) parameters));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(cls.getSimpleName()+" object could not be instantiated. (probably because the previous flag was not added in the database)");
        } catch (RuntimeException e) { System.err.println(e.getMessage()); }
    }

    /**
     * Loads data from a file. The order of the entries doesn't matter as long as every entry has the correct flag associated.
     * @param fileName the name of the file
     */
    @Audited public void load (String fileName) {
        Map<Class<? extends TableRow>, ArrayList<String[]>> parameters= new HashMap<>() {{
            for (String name: classes.keySet()) {
                put(classes.get(name), new ArrayList<>());
            }
        }};
        try {
            Scanner file = new Scanner(new File(fileName));
            Class<? extends TableRow> currentClass = null;
            while (file.hasNextLine()) {
                String line = file.nextLine().strip();
                if (!line.equals("")) {
                    if (classes.containsKey(line)) {
                        currentClass = classes.get(line);
                        continue;
                    }
                    if (currentClass == null) {
                        System.err.println("invalid flag '"+line+'\'');
                        continue;
                    }
                    String[] params = Arrays.stream(line.split(",")).map(String::strip).toArray(String[]::new);
                    if (!params[0].matches("-?(0|[1-9]\\d*)")) {
                        System.err.println("Invalid id/flag '"+params[0]+"'");
                        continue;
                    }
                    parameters.get(currentClass).add(params);
                }
            }
        }
        catch (FileNotFoundException e) { System.err.println("File not found"); }
        for (String key: priority) {
            parameters.get(classes.get(key)).stream().sorted(Comparator.comparingInt(x -> Integer.parseInt(x[0]))).forEach(x -> instantiateObject(classes.get(key), x));
        }
        Audit.entry("Data loaded from "+fileName, AuditLevel.LOAD);
    }

    /**
     * Saves the database to multiple files. Each file has the name <b>className</b>s.csv.
     * @implNote The lines are generally ordered by id but this is not guaranteed as it depends entirely of the Map::values implementation
     */
    @Audited public void save () {
        try {
            for (String flag: classes.keySet()) {
                FileWriter file = new FileWriter(classes.get(flag).getSimpleName()+"s.csv");
                file.write(flag+'\n');
                for (TableRow row: maps.get(classes.get(flag)).values()) { file.write(String.join(",", row.getData())+'\n'); }
                file.flush();
                file.close();
                Audit.entry(classes.get(flag).getSimpleName()+"s saved to "+classes.get(flag).getSimpleName()+"s.csv", AuditLevel.SAVE);
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Saves the database to a single file. The tables are in the dependency order(classes with no dependencies go first).
     * @param fileName the name of the file
     * @implNote The lines are generally ordered by id but this is not guaranteed as it depends entirely of the Map::values implementation
     */
    @Audited public void save (String fileName) {
        try {
            FileWriter file = new FileWriter(fileName);
            for (String flag: priority) {
                file.write(flag+'\n');
                for (TableRow row: maps.get(classes.get(flag)).values()) { file.write(String.join(",", row.getData())+'\n'); }
            }
            file.flush();
            file.close();
            Audit.entry("Data saved to "+fileName, AuditLevel.SAVE);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void print() {
        for (Class<?> cls: maps.keySet()) { System.out.format("%15s%s%n", cls.getSimpleName()+":   ", maps.get(cls)); }
    }
}
