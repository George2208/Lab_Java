package com.company;

import java.util.HashSet;
import java.util.Set;

public class Attendant {
    private static int nextID = 1;
    private final int attenantID, studentID, courseID;
    private final Set<Integer> grades = new HashSet<>();

    Attendant(int studentID, int courseID) {
        attenantID = nextID++;
        this.studentID = studentID;
        this.courseID = courseID;
    }

    public int getAttenantID() { return attenantID; }
    public int getStudentID() { return studentID; }
    public int getCourseID() { return courseID; }

    public Set<Integer> getGrades() { return grades; }
    public void addGrade(int gradeID) { grades.add(gradeID); }
    public void delGrade(int gradeID) { grades.remove(gradeID); }

    @Override
    public String toString() {
        return "Attendant{" +
                "attenantID=" + attenantID +
                ", studentID=" + studentID +
                ", courseID=" + courseID +
                '}';
    }
}
