package com.company;

import java.util.HashSet;
import java.util.Set;

public class Student extends Person {
    private static int nextID = 1;
    private final int studentID;
    private final Set<Integer> attendances = new HashSet<>();

    Student(String fName, String lName) {
        super(fName, lName);
        studentID = nextID++;
    }

    public int getStudentID() { return studentID; }

    public Set<Integer> getAttendances() { return attendances; }
    public void addAttendance(int attendantID) { attendances.add(attendantID); }
    public void delAttendance(int attendantID) { attendances.remove(attendantID); }

    @Override
    public String toString() {
        return "Student{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", studentID=" + studentID +
                ", attendances=" + attendances +
                '}';
    }
}