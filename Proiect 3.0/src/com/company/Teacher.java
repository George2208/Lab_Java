package com.company;

import java.util.HashSet;

public class Teacher extends Person {
    private static int nextID = 1;
    public Teacher(String fName, String lName) {
        super(nextID++, fName, lName);
        this.isDependency.put(Course.class, new HashSet<>());
    }
    @ID public Teacher(String newID, String fName, String lName) {
        super(Integer.parseInt(newID), fName, lName);
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.isDependency.put(Course.class, new HashSet<>());
    }
    @Override public String toString() { return super.toString().formatted(""); }
}
