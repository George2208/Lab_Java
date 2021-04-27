package com.company;

import java.util.*;

public class Student extends Person {
    private static int nextID = 1;
    private final Set<Integer> attendances = new HashSet<>();
    public Student(String fName, String lName) { super(nextID++, fName, lName); }
    @ID public Student(String newID, String fName, String lName) throws RuntimeException {
        super(Integer.parseInt(newID), fName, lName);
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
    }
    @Override public List<String> getData() {
        return List.of(String.valueOf(this.id), this.fName, this.lName);
    }
    @Override public Map<Class<? extends TableRow>, Collection<Integer>> isDependency() {
        return new HashMap<>(){{ put(Attendant.class, attendances); }};
    }
    @Override public void removeDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Attendant.class) { attendances.remove(id); }
    }
    @Override public void addDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Attendant.class) { attendances.add(id); }
    }
    @Override public String toString() {
        return "Student{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", id=" + id +
                ", attendances=" + attendances +
                '}';
    }
}
