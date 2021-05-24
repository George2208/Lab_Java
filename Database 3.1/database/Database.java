package database;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

public class Database {
    public @interface Audited { }
    protected final Map<Class<?>, Table> tables = new HashMap<>();
    private final Connection connection;
    public Database(String url) {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private Row _copy(Row obj) {
        try {
            return (Row) tables.get(obj.getClass()).constructor.newInstance(new ArrayList<>(){{
                add(obj.id);
                for(Method getter: tables.get(obj.getClass()).getters.values())
                    add(getter.invoke(obj));
            }}.toArray());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            System.err.println("!!  Could not copy object of type "+obj.getClass().getSimpleName()+", returning null  !!");
            return null;
        }
    }
    @Audited private void _SQL_Create(String tableName, SortedSet<String> columns, Map<String, String> foreignKeys) {
        try {
            StringBuilder sb = new StringBuilder("create table "+tableName+" (id int primary key");
            for(String column: columns) {
                if(foreignKeys.containsKey(column))
                    sb.append(", ").append(column).append(" int, foreign key (").append(column).append(") references ").append(foreignKeys.get(column)).append("(id)");
                else
                    sb.append(", ").append(column).append(" text");
            }
            sb.append(");");
            connection.prepareStatement(sb.toString()).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Audited private void _SQL_Insert(String tableName, int id, Map<String, String> data, Collection<String> foreignKeys) {
        try {
            StringBuilder sb = new StringBuilder("insert into "+tableName+" values ("+id);
            for(Object column: data.keySet().stream().sorted().toArray())
                if(foreignKeys.contains((String) column))
                    sb.append(',').append(data.get(column));
                else
                    sb.append(",'").append(data.get(column)).append("'");
            sb.append(");");
            connection.prepareStatement(sb.toString()).execute();
            Audit.entry("Inserted row with id "+id+" in "+tableName, AuditLevel.INSERT);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Audited private void _SQL_Delete(String tableName, int id) {
        try {
            connection.prepareStatement("delete from "+tableName+" where id="+id+";").execute();
            Audit.entry("Deleted row with id "+id+" in "+tableName, AuditLevel.DELETE);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Audited private void _SQL_Update(String tableName, int id, String column, String newValue, boolean isFK) {
        try {
            if(isFK)
                connection.prepareStatement("update "+tableName+" set "+column+"="+newValue+" where id="+id).execute();
            else
                connection.prepareStatement("update "+tableName+" set "+column+"='"+newValue+"' where id="+id).execute();
            Audit.entry("Updated row with id "+id+" in "+tableName, AuditLevel.UPDATE);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Audited private void _addTable(Class<? extends Row> cls, boolean withSql) {
        if(tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" already has a table declared");
        String tableName = null;
        Constructor<?> tableConstructor = null;
        SortedMap<String, Method> tableGetters = new TreeMap<>(String::compareTo);
        Map<String, Method> tableSetters = new HashMap<>();
        Map<Class<? extends Row>, String> tableDependencies = new HashMap<>();
        for(Constructor<?> constructor: cls.getDeclaredConstructors())
            for(Annotation annotation: constructor.getDeclaredAnnotations())
                if(annotation.annotationType().equals(TableObject.class)) {
                    if(tableConstructor != null)
                        throw new RuntimeException("Multiple annotated constructors");
                    tableName = ((TableObject) annotation).tableName();
                    tableConstructor = constructor;
                }
        for(Method method: cls.getMethods())
            for(Annotation annotation: method.getAnnotations()) {
                if(annotation.annotationType().equals(Getter.class)) {
                    tableGetters.put(((Getter) annotation).alias(), method);
                    if(!((Getter) annotation).target().equals(Row.class)) {
                        if(method.getParameterTypes().length > 0)
                            throw new RuntimeException("Getter "+method.getName()+" should not take any arguments");
                        if(method.getReturnType() != String.class)
                            throw new RuntimeException("Getter "+method.getName()+" should return a string");
                        tableDependencies.put(((Getter) annotation).target(), ((Getter) annotation).alias());
                    }
                }
                if(annotation.annotationType().equals(Setter.class)) {
                    if(method.getParameterTypes().length != 1)
                        throw new RuntimeException("Getter "+method.getName()+" should take exactly one argument");
                    if(method.getParameterTypes()[0] != String.class)
                        throw new RuntimeException("Getter "+method.getName()+" should take a string as argument");
                    tableSetters.put(((Setter) annotation).alias(), method);
                }
            }
        for(Class<?> dependency: tableDependencies.keySet())
            if(!tables.containsKey(dependency))
                throw new RuntimeException(cls.getSimpleName()+" is dependent on "+dependency.getSimpleName()+" which is not in the database");
        for(Class<?> dependency: tableDependencies.keySet())
            tables.get(dependency).isDependency.add(cls);
        if(withSql) {
            Map<String, String> sqlDependencies = new HashMap<>(){{
                for(Class<?> dependencyType: tables.keySet())
                    if(tableDependencies.containsKey(dependencyType))
                        put(tableDependencies.get(dependencyType), tables.get(dependencyType).name);
            }};
            _SQL_Create(tableName, (SortedSet<String>) tableGetters.keySet(), sqlDependencies);
        }
        tables.put(cls, new Table(tableName, tableConstructor, tableGetters, tableSetters, tableDependencies));
        Audit.entry("Table "+tables.get(cls).name+" created", AuditLevel.CREATE_TABLE);
    }
    private Map<String, String> getColumns(Row obj) throws InvocationTargetException, IllegalAccessException {
        Map<String, String> columns = new HashMap<>();
        columns.put(obj.getClass().getSimpleName(), String.valueOf(obj.id));
        for(String column: tables.get(obj.getClass()).getters.keySet()) {
            String value = (String) tables.get(obj.getClass()).getters.get(column).invoke(obj);
            columns.put(obj.getClass().getSimpleName()+'.'+column, value);
        }
        for(Class<?> dependency: tables.get(obj.getClass()).dependencies.keySet())
            columns.putAll(getColumns(tables.get(dependency).rows.get(Integer.parseInt((String) tables.get(obj.getClass()).getters.get(tables.get(obj.getClass()).dependencies.get(dependency)).invoke(obj)))));
        return columns;
    }
    protected Map<Integer, List<Row>> _join(Class<? extends Row> strong, Class<? extends Row> weak) {
        try {
            return new HashMap<>(){{
                for(Row row1: tables.get(strong).rows.values()) {
                    for(Row row2: select(weak, new HashMap<>(){{
                        put(tables.get(weak).dependencies.get(strong), x -> String.valueOf(row1.id).equals(x));
                    }})) {
                        if(!containsKey(row1.id))
                            put(row1.id, new ArrayList<>());
                        get(row1.id).add(row2);
                    }
                }
            }};
        } catch (Throwable e) {
            throw new RuntimeException("Class "+weak.getSimpleName()+" is not dependent on "+strong.getSimpleName());
        }
    }
    public void loadTable(Class<? extends Row> cls, String tableName) {
        try {
            ResultSet resultSet = connection.prepareStatement("select * from "+tableName+" order by id;").executeQuery();
            int nrColumns = resultSet.getMetaData().getColumnCount();
            List<Object[]> data = new ArrayList<>();
            while(resultSet.next()) {
                Object[] parameters = new Object[nrColumns];
                parameters[0] = resultSet.getInt(1);
                for(int i=1; i<nrColumns; i++)
                    parameters[i] = resultSet.getString(i+1);
                data.add(parameters);
            }
            if(!tables.containsKey(cls))
                _addTable(cls, false);
            for(Object[] parameters: data)
                tables.get(cls).insertWithID(parameters);
            System.out.println("Table "+tableName+" loaded");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException | RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
    public void describeTable(Class<?> cls) { tables.get(cls).describe(); }
    public void describeTables() {
        System.out.println("Database table descriptions:\n");
        for(Class<?> table: tables.keySet()) {
            describeTable(table);
            System.out.println();
        }
    }
    public void showTable(Class<?> cls) { tables.get(cls).show(); }
    public void showTables() {
        System.out.println("Database table rows:\n");
        for(Class<?> table: tables.keySet()) {
            System.out.println(tables.get(table).name+":");
            showTable(table);
        }
    }
    public void addTable(Class<? extends Row> cls) {
        try {
            _addTable(cls, true);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
    @Audited public void dropTable(Class<?> cls) {
        if(tables.get(cls).isDependency.size() > 0)
            System.err.println("Class "+cls.getSimpleName()+" is dependency for other tables "+tables.get(cls).isDependency);
        else
            try {
                connection.prepareStatement("drop table "+tables.get(cls).name+";").execute();
                for(Class<?> dependency: tables.get(cls).dependencies.keySet())
                    tables.get(dependency).isDependency.remove(cls);
                Audit.entry("Table "+tables.get(cls).name+" dropped", AuditLevel.DROP_TABLE);
                tables.remove(cls);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
    }
    public void dropTables() {
        while (tables.size() > 0) {
            Set<Class<?>> nonConcurrent = new HashSet<>(tables.keySet());
            for(Class<?> cls: nonConcurrent)
                if(tables.get(cls).isDependency.size() == 0)
                    dropTable(cls);
        }
    }
    public <T extends Row> int insert(T obj) {
        try {
            if(!tables.containsKey(obj.getClass()))
                throw new RuntimeException("Class "+obj.getClass().getSimpleName()+" doesn't have a table associated");
            Table table = tables.get(obj.getClass());
            Map<Class<?>, Integer> dependencies = table.getDependencyIDs(obj);
            for(Class<?> dependency: dependencies.keySet())
                if(!tables.get(dependency).rows.containsKey(dependencies.get(dependency)))
                    throw new RuntimeException("Missing dependency for "+obj.getClass().getSimpleName()+" ("+dependency.getSimpleName()+", "+dependencies.get(dependency)+")");
            SortedMap<String, String> data = new TreeMap<>();
            for(String column: table.getters.keySet())
                data.put(column, (String) table.getters.get(column).invoke(obj));
            _SQL_Insert(table.name, table.nextID, data, table.dependencies.values());
            return tables.get(obj.getClass()).insert(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }
    public Row select(Class<?> cls, int id) {
        if(!tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" doesn't have a table associated");
        return _copy(tables.get(cls).selectID(id));
    }
    public Set<Integer> filteredIDs(Class<?> cls, Map<String, Predicate<Object>> filters) {
        if(!tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" doesn't have a table associated");
        try {
            return tables.get(cls).filteredIDs(filters);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    public Set<Row> select(Class<?> cls, Map<String, Predicate<Object>> filters) {
        if(!tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" doesn't have a table associated");
        try {
            Set<Row> set = new HashSet<>();
            Table table = tables.get(cls);
            for(int id: table.filteredIDs(filters))
                set.add(table.rows.get(id));
            return set;
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    public void delete(Class<?> cls, int id) {
        if(!tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" doesn't have a table associated");
        for(Class<?> dependency: tables.get(cls).isDependency) {
            for (int dependencyID: filteredIDs(dependency, new HashMap<>(){{
                put(tables.get(dependency).dependencies.get(cls), x -> x.equals(String.valueOf(id)));
            }}))
                delete(dependency, dependencyID);
        }
        _SQL_Delete(tables.get(cls).name, id);
        tables.get(cls).delete(id);
    }
    public void update(Class<?> cls, int id, String column, String newValue) {
        if(!tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" doesn't have a table associated");
        Table table = tables.get(cls);
        if(!table.getters.containsKey(column))
            throw new RuntimeException("Table "+table.name+" doesn't have a column named "+column);
        if(!table.rows.containsKey(id))
            throw new RuntimeException("Table "+table.name+" doesn't have a row with id "+id);
        try {
            if(table.dependencies.containsValue(column)) {
                System.out.println("Column "+column+" is foreign key");
                if(!tables.get(table.rowFK(column)).rows.containsKey(Integer.parseInt(newValue)))
                    throw new RuntimeException(newValue+" is not a valid foreign key ("+column+")");
            }
            _SQL_Update(table.name, id, column, newValue, table.dependencies.containsValue(column));
            table.setters.get(column).invoke(table.rows.get(id), newValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("!! Column could not be modified !!");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
    public Set<Map<String, String>> allInfo(Class<?> cls) {
        if (!tables.containsKey(cls))
            throw new RuntimeException("Class "+cls.getSimpleName()+" doesn't have a table associated");
        return new HashSet<>(){{
            for(int key: tables.get(cls).rows.keySet()) {
                try {
                    add(getColumns(tables.get(cls).rows.get(key)));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }};
    }
}
