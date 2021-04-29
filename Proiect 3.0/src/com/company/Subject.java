package com.company;

import java.util.HashSet;

public class Subject extends TableRow {
    private static int nextID = 1;
    private String name;
    public Subject(String name) {
        super(nextID++);
        this.name = name;
        this.isDependency.put(Course.class, new HashSet<>());
    }
    @ID public Subject(String newID, String name) throws RuntimeException {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.name = name;
        this.isDependency.put(Course.class, new HashSet<>());
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @Override public String toString() { return super.toString().formatted(", name="+name); }
}
