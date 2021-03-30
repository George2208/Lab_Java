package com.company;

import java.util.HashMap;
import java.util.Map;

public class Student extends Person {
    private static int nextID = 1;
    private final int studentID;
    private final Map<Integer, Integer> attendances = new HashMap<>();

    Student(String fName, String lName) {
        super(fName, lName);
        studentID = nextID++;
    }

    public int getStudentID() { return studentID; }
    public Integer[] getAttendances() { return attendances.values().toArray(new Integer[0]); }
    public Integer[] getCourses() { return attendances.keySet().toArray(new Integer[0]); }
    public int getAttendantForCourse(int courseId) {
        if(!attendances.containsKey(courseId)) { throw new RuntimeException("Invalid course for student"); }
        return attendances.get(courseId);
    }
    public void addAttendance(int courseID, int attendantID) {
        if(attendances.containsKey(courseID)) { throw new RuntimeException("Student already at this course"); }
        attendances.put(courseID, attendantID);
    }
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