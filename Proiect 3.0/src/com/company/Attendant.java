package com.company;

import java.util.HashSet;

public class Attendant extends TableRow {
    private static int nextID = 1;
    public Attendant(int studentID, int courseID) {
        super(nextID++);
        this.hasDependency.put(Student.class, studentID);
        this.hasDependency.put(Course.class, courseID);
        this.isDependency.put(Grade.class, new HashSet<>());
    }
    @ID public Attendant(String newID, String studentID, String courseID) {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.hasDependency.put(Student.class, Integer.parseInt(studentID));
        this.hasDependency.put(Course.class, Integer.parseInt(courseID));
        this.isDependency.put(Grade.class, new HashSet<>());
    }
    @Override public String toString() { return super.toString().formatted(""); }
}
