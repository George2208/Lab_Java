package com.company;

import java.util.HashSet;
import java.util.Set;

public class Subject {
    private static int nextID = 1;
    private final int classID;
    private String subjectName;
    private final Set<Integer> teachers = new HashSet<>();
    private final Set<Integer> courses = new HashSet<>();

    Subject(String className) {
        classID = nextID++;
        this.subjectName = className;
    }

    public int getSubjectID() { return classID; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String className) { this.subjectName = className; }

    public Set<Integer> getTeachers() { return teachers; }
    public void addTeacher(int subjectID) { teachers.add(subjectID); }
    public void delTeacher(int subjectID) { teachers.remove(subjectID); }

    public Set<Integer> getCourses() { return courses; }
    public void addCourse(int courseID) { courses.add(courseID); }
    public void delCourse(int courseID) { courses.remove(courseID); }

    @Override
    public String toString() {
        return "Subject{" +
                "classID=" + classID +
                ", subjectName='" + subjectName + '\'' +
                ", teachers=" + teachers +
                '}';
    }
}
