package com.company;

import java.util.HashSet;

public class Student extends Person {
    private static int nextID = 1;
    public Student(String fName, String lName) {
        super(nextID++, fName, lName);
        this.isDependency.put(Attendant.class, new HashSet<>());
    }
    @ID public Student(String newID, String fName, String lName) throws RuntimeException {
        super(Integer.parseInt(newID), fName, lName);
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.isDependency.put(Attendant.class, new HashSet<>());
    }
    @Override public String toString() { return super.toString().formatted(""); }
}
