package com.company;

import java.util.*;

public class Attendant extends TableRow {
    private static int nextID = 1;
    private final int studentID, courseID;
    private final Set<Integer> grades = new HashSet<>();
    public Attendant(int studentID, int courseID) {
        super(nextID++);
        this.studentID = studentID;
        this.courseID = courseID;
    }
    @ID public Attendant(String newID, String studentID, String courseID) {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.studentID = Integer.parseInt(studentID);
        this.courseID = Integer.parseInt(courseID);
    }
    @Override public List<String> getData() {
        return List.of(String.valueOf(this.id), String.valueOf(this.studentID), String.valueOf(this.courseID));
    }
    @Override public Map<Class<? extends TableRow>, Collection<Integer>> isDependency() {
        return new HashMap<>(){{ put(Grade.class, grades); }};
    }
    @Override public Map<Class<? extends TableRow>, Integer> hasDependency() {
        return new HashMap<>(){{
            put(Student.class, studentID);
            put(Course.class, courseID);
        }};
    }
    @Override public void removeDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Grade.class) { grades.remove(id); }
    }
    @Override public void addDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Grade.class) { grades.add(id); }
    }
    @Override public String toString() {
        return "Attendant{" +
                "id=" + id +
                ", studentID=" + studentID +
                ", courseID=" + courseID +
                ", grades=" + grades +
                '}';
    }
}
