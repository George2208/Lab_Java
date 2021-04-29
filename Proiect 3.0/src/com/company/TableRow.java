package com.company;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TableRow {
    protected final int id;
    protected final Map<Class<? extends TableRow>, Collection<Integer>> isDependency = new HashMap<>();
    protected final Map<Class<? extends TableRow>, Integer> hasDependency = new HashMap<>();

    /**
     * Creates a new TableRow object.
     * @param id the new object's id
     * @implSpec Every derived class must have exactly one constructor annotated with @ID which takes only strings as arguments
     * @implNote In order for different classes to be able to have identical ids each derived class should implement it's own unique id management and pass it to super() constructor
     */
    protected TableRow(int id) {
        this.id = id;
        Constructor<?> constructor = null;
        for (Constructor<?> ctor: this.getClass().getConstructors()) {
            for (Annotation annotation: ctor.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(ID.class)) {
                    if (constructor != null) {
                        throw new RuntimeException("Cannot instantiate objects of class "+this.getClass().getSimpleName()+" because multiple constructors are annotated with @ID");
                    }
                    constructor = ctor;
                }
            }
        }
        if (constructor == null) {
            throw new RuntimeException("Cannot instantiate objects of class "+this.getClass().getSimpleName()+" because no constructor is annotated with @ID");
        }
    }

    public int id() { return id; }
    public Map<Class<? extends TableRow>, Collection<Integer>> isDependency() { return isDependency; }
    public Map<Class<? extends TableRow>, Integer> hasDependency() { return hasDependency; }

    public List<String> getData() {
        List<String> columns = new ArrayList<>();
        columns.add(String.valueOf(id));
        for (Class<?> cls: hasDependency.keySet().stream().sorted(Comparator.comparing(Class::getSimpleName)).collect(Collectors.toList())) {
            columns.add(String.valueOf(hasDependency.get(cls)));
        }
        return columns;
    }
    public void removeDependency(Class<? extends TableRow> cls, Integer id) {
        if (!isDependency.containsKey(cls)) {
            throw new RuntimeException(cls.getSimpleName()+" is not a dependency for "+getClass().getSimpleName());
        }
        isDependency.get(cls).remove(id);
    }
    public void addDependency(Class<? extends TableRow> cls, Integer id) {
        if (!isDependency.containsKey(cls)) {
            throw new RuntimeException(cls.getSimpleName()+" is not a dependency for "+getClass().getSimpleName());
        }
        isDependency.get(cls).add(id);
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(this.getClass().getSimpleName() + "{id=" + id + "%s");
        for (Class<?> cls: hasDependency.keySet().stream().sorted(Comparator.comparing(Class::getSimpleName)).collect(Collectors.toList()))
            str.append(", ").append(cls.getSimpleName()).append("ID=").append(hasDependency.get(cls));
        for (Class<?> cls: isDependency.keySet().stream().sorted(Comparator.comparing(Class::getSimpleName)).collect(Collectors.toList()))
            str.append(", ").append(cls.getSimpleName()).append("IDs=").append(isDependency.get(cls));
        str.append('}');
        return str.toString();
    }
}
