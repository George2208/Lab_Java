package com.company;

import java.util.*;

public class Teacher extends Person {
    private static int nextID = 1;
    final Set<Integer> courses = new HashSet<>();
    public Teacher(String fName, String lName) { super(nextID++, fName, lName); }
    @ID public Teacher(String newID, String fName, String lName) {
        super(Integer.parseInt(newID), fName, lName);
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
    }
    @Override public List<String> getData() {
        return List.of(String.valueOf(this.id), this.fName, this.lName);
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
        return "Teacher{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", id=" + id +
                ", courses=" + courses +
                '}';
    }
}
