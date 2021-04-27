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
    static class PairListClass<T> extends Pair<List<T>, Class<T>> {
        public PairListClass(List<T> list, Class<T> cls) { super(list, cls); }
    }
    private final Map<String, Class<? extends TableRow>> classes = new HashMap<>() {{
        put("-Students", Student.class);
        put("-Teachers", Teacher.class);
        put("-Subjects", Subject.class);
        put("-Courses", Course.class);
        put("-Attendants", Attendant.class);
        put("-Grades", Grade.class);
    }};
    Map<Class<? extends TableRow>, Map<Integer, TableRow>> maps = new HashMap<>() {{
        for (Class<? extends TableRow> cls: classes.values())
            put(cls, new HashMap<>());
    }};

    public <T extends TableRow> void add(T row) {
        for (Pair<Class<? extends TableRow>, Integer> dependency: row.hasDependency()) {
            if (!maps.get(dependency.fst).containsKey(dependency.snd)) {
                System.err.println("Invalid "+row.getClass().getSimpleName()+": "+row+" (no "+dependency.fst.getSimpleName()+" with id "+dependency.snd+")");
                return;
            }
        }
        for (Pair<Class<? extends TableRow>, Integer> dependency: row.hasDependency()) {
            maps.get(dependency.fst).get(dependency.snd).addDependency(row.getClass(), row.id());
        }
        maps.get(row.getClass()).put(row.id, row);
    }
    public void delete(Class<? extends TableRow> cls, Integer id) {
        if (!maps.get(cls).containsKey(id)) {
            System.err.println("No object found while trying to delete. Circular dependency?");
            return;
        }
        System.out.println("Delete for "+cls.getSimpleName()+ " with id "+id);
        for (Pair<Class<? extends TableRow>, Integer> dependency: maps.get(cls).get(id).hasDependency()) {
            System.out.println("   Remove dependency ("+cls.getSimpleName()+" "+id+") from "+dependency.fst.getSimpleName()+" "+dependency.snd);
            maps.get(dependency.fst).get(dependency.snd).removeDependency(cls, id);
        }
        for (Pair<Class<? extends TableRow>, Collection<Integer>> dependency: maps.get(cls).get(id).isDependency()) {
            for (int dependencyID: dependency.snd) {
                System.out.println("   Deleting dependency "+dependency.fst.getSimpleName()+" "+dependencyID+" (from "+dependency.snd+")");
                delete(dependency.fst, dependencyID);  // TODO: Concurrent modification
            }
        }
        maps.get(cls).remove(id);
        Audit.entry("Deleted "+cls.getSimpleName()+ " " +id);
    }
    public static <T extends TableRow> void loadAux(String row, PairListClass<T> pair) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Constructor<?> i: pair.snd.getConstructors()) {
            for (Annotation annotation: i.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(ID.class)) {
                    Object obj = i.newInstance((Object[]) Arrays.stream(row.split(",")).map(String::strip).toArray(String[]::new));
                    pair.fst.add(pair.snd.cast(obj));
                    return;
                }
            }
        }
        System.err.println("No constructor found");
    }
    public void load (String fileName) {
        Map<String, PairListClass<? extends TableRow>> flags= new HashMap<>() {{
            for (String name: classes.keySet()) {
                put(name, new PairListClass<>(new ArrayList<>(), classes.get(name)));
            }
        }};
        try {
            Scanner file = new Scanner(new File(fileName));
            String currentTable = "";
            while (file.hasNextLine()) {
                String line = file.nextLine().strip();
                if (!line.equals("")) {
                    if(flags.containsKey(line)) { currentTable = line; }
                    else {
                        try { loadAux(line, flags.get(currentTable)); }
                        catch (NullPointerException e) { System.err.println("Invalid flag"); }
                        catch (IndexOutOfBoundsException e) { System.err.println("Invalid number of arguments for "+flags.get(currentTable).snd.getSimpleName()+ " constructor"); }
                        catch (NumberFormatException e) { System.err.println("Invalid id (NAN)"); }
                        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) { System.err.println("!!!Cast error!!! "+e.getMessage()); }
                        catch (RuntimeException e) { System.err.println(e.getMessage()); }
                    }
                }
            }
        }
        catch (FileNotFoundException e) { System.err.println("File not found"); }
        for (String key: new String[]{"-Students", "-Teachers", "-Subjects", "-Courses", "-Attendants", "-Grades"}) {
            for (Object obj: flags.get(key).fst) {     // TODO: Sort list
                add(flags.get(key).snd.cast(obj));
            }
        }
        Audit.entry("Data loaded from "+fileName);
    }
    public void save () {
        try {
            for (String flag: classes.keySet()) {
                FileWriter file = new FileWriter(classes.get(flag).getSimpleName()+"s.csv");
                file.write(flag+'\n');
                for (TableRow row: maps.get(classes.get(flag)).values()) {
                    file.write(String.join(",", row.getData())+'\n');
                }
                file.flush();
                file.close();
                Audit.entry(classes.get(flag).getSimpleName()+"s saved to "+classes.get(flag).getSimpleName()+"s.csv");
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void save (String fileName) {
        try {
            FileWriter file = new FileWriter(fileName);
            for (String flag: new String[]{"-Students", "-Teachers", "-Subjects", "-Courses", "-Attendants", "-Grades"}) {
                file.write(flag+'\n');
                for (TableRow row: maps.get(classes.get(flag)).values()) {
                    file.write(String.join(",", row.getData())+'\n');
                }
            }
            file.flush();
            file.close();
            Audit.entry("Data saved to "+fileName);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void debug() {
        for (var table: maps.values()) {
            System.out.println(table);
        }
    }

    public void showTables () {
        for (Map<Integer, ? extends TableRow> map: maps.values()) {
            System.out.println();
            for (TableRow obj: map.values()) {
                String[] row = obj.getData().toArray(new String[0]);
                System.out.format("%15s".repeat(row.length)+"%n", (Object[]) row);
            }
        }
    }
}
