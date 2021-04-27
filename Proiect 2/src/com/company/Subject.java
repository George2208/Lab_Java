package com.company;

import java.util.*;

public class Subject extends TableRow {
    private static int nextID = 1;
    private final String name;
    final Set<Integer> courses = new HashSet<>();
    public Subject(String name) {
        super(nextID++);
        this.name = name;
    }
    @ID public Subject(String newID, String name) throws RuntimeException {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.name = name;
    }
    public String getName() { return name; }
    @Override public List<String> getData() {
        return List.of(String.valueOf(this.id), this.name);
    }
    @Override public Map<Class<? extends TableRow>, Collection<Integer>> isDependency() {
        return new HashMap<>(){{ put(Course.class, courses); }};
    }
    @Override public void removeDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Course.class) { courses.remove(id); }
    }
    @Override public void addDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Course.class) { courses.add(id); }
    }
    @Override public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
