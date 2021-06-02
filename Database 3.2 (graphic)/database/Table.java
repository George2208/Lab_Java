package database;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class Table {
    public int nextID = 1;
    public final String name;
    public final Constructor<?> constructor;
    public final SortedMap<Integer, Row> rows;
    public final SortedMap<String, Method> getters;
    public final Map<String, Method> setters;
    public final Map<Class<? extends Row>, String> dependencies;
    public final Set<Class<? extends Row>> isDependency;

    public Table(String tableName,
                 Constructor<?> tableConstructor,
                 SortedMap<String, Method> tableGetters,
                 Map<String, Method> tableSetters,
                 Map<Class<? extends Row>, String> tableDependencies) {
        if(tableConstructor == null)
            throw new RuntimeException("No annotated constructor");
        for(String column: tableSetters.keySet())
            if(!tableGetters.containsKey(column))
                throw new RuntimeException(column+" has a setter declared without a getter");
        for(String column: tableGetters.keySet())
            if(!tableSetters.containsKey(column))
                throw new RuntimeException(column+" has a getter declared without a setter");
        name = tableName;
        constructor = tableConstructor;
        getters = tableGetters;
        setters = tableSetters;
        dependencies = tableDependencies;
        rows = new TreeMap<>();
        isDependency = new HashSet<>();
    }
    public Map<Class<?>, Integer> getDependencyIDs(Row obj) {
        try {
            return new HashMap<>(){{
                for(Class<?> dependency: dependencies.keySet())
                    put(dependency, Integer.parseInt((String) getters.get(dependencies.get(dependency)).invoke(obj)));
            }};
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    public void insertWithID(Object[] parameters) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if((int) parameters[0] < nextID)
            throw new RuntimeException("Invalid id");
        Row obj = (Row) constructor.newInstance(new ArrayList<>(){{ addAll(Arrays.asList(parameters)); }}.toArray());
        nextID = (int) parameters[0] + 1;
        rows.put(obj.id, obj);
    }
    public int insert(Object[] parameters) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println(Arrays.toString(new ArrayList<>() {{
            add(nextID++);
            addAll(Arrays.asList(parameters));
        }}.toArray()));
        Row obj = (Row) constructor.newInstance(new ArrayList<>(){{
            add(nextID++);
            addAll(Arrays.asList(parameters));
        }}.toArray());
        rows.put(obj.id, obj);
        return obj.id;
    }
    public int insert(Row obj) {
        try {
            Row rez = (Row) constructor.newInstance(new ArrayList<>(){{
                add(nextID++);
                for(Method getter: getters.values())
                    add(getter.invoke(obj));
            }}.toArray());
            rows.put(rez.id, rez);
            return rez.id;
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            System.err.println("!!  Could not copy object of type "+obj.getClass().getSimpleName()+", returning null  !!");
            return -1;
        }
    }
    public void delete(int id) {
        if(!rows.containsKey(id))
            System.out.println("Unnecessary delete of key "+id+" from "+name);
        rows.remove(id);
    }
    public void update(int id, String column, String newValue) throws InvocationTargetException, IllegalAccessException {
        if(!getters.containsKey(column))
            throw new RuntimeException("Table "+name+" doesn't have a column named "+column);
        if(!rows.containsKey(id))
            throw new RuntimeException("Table "+name+" doesn't have a row with id "+id);
        setters.get(column).invoke(rows.get(id), newValue);
    }
    public Row selectID(int id) {
        if(!rows.containsKey(id))
            return null;
        return rows.get(id);
    }
    public SortedSet<Integer> filteredIDs(Map<String, Predicate<Object>> filters) throws RuntimeException {
        try {
            SortedSet<Integer> ids = new TreeSet<>(rows.keySet());
            for(String column: filters.keySet())
                for(int id: rows.keySet())
                    try {
                        if(!filters.get(column).test(getters.get(column).invoke(rows.get(id))))
                            ids.remove(id);
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Class cast exception (probably because column "+column+" has an incompatible type)");
                    }
            return ids;
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public Class<?> rowFK(String column) {
        for(Class<?> cls: dependencies.keySet())
            if(dependencies.get(cls).equals(column))
                return cls;
        throw new RuntimeException("Table "+name+" doesn't have any dependencies associated to column "+column);
    }
    public void describe() {
        System.out.printf("Name: %s (class %s, %s rows)\nColumns(%s): %s\nWeak dependencies: %s\nStrong dependencies: %s\n",
                name, constructor.getDeclaringClass().getSimpleName(), rows.size(), getters.keySet().size(), getters.keySet(),
                Arrays.toString(isDependency.stream().map(Class::getSimpleName).toArray()),
                Arrays.toString(dependencies.keySet().stream().map(x -> "" + x.getSimpleName() + " (" + dependencies.get(x) + ")").toArray())
        );
    }
    public void show() {
        try {
            System.out.printf("%5s  ", "ID");
            for(String col: getters.keySet())
                System.out.printf("%-15s", col);
            for(Row row: rows.values()) {
                System.out.println();
                System.out.printf("%5s  ", row.id);
                for(String col: getters.keySet())
                    System.out.printf("%-15s", getters.get(col).invoke(row));
            }
            System.out.println();
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("!!  Table show exception  !!");
        }

    }
}
