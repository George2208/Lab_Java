package com.company;

import java.util.*;

public class Course extends TableRow {
    private static int nextID = 1;
    public Course(int teacherID, int subjectID) {
        super(nextID++);
        this.hasDependency.put(Subject.class, subjectID);
        this.hasDependency.put(Teacher.class, teacherID);
        this.isDependency.put(Attendant.class, new HashSet<>());
    }
    @ID public Course(String newID, String teacherID, String subjectID) {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.hasDependency.put(Subject.class, Integer.parseInt(subjectID));
        this.hasDependency.put(Teacher.class, Integer.parseInt(teacherID));
        this.isDependency.put(Attendant.class, new HashSet<>());
    }
    @Override public String toString() { return super.toString().formatted(""); }
}
