package com.company;

import java.util.HashSet;
import java.util.Set;

public class Course {
    private static int nextID = 1;
    private final int courseID, subjectID, teacherID;
    private final Set<Integer> attendants = new HashSet<>();

    Course(int subjectID, int teacherID) {
        courseID = nextID++;
        this.subjectID = subjectID;
        this.teacherID = teacherID;
    }

    public int getCourseID() { return courseID; }
    public int getSubjectID() { return subjectID; }
    public int getTeacherID() { return teacherID; }

    public Set<Integer> getAttendants() { return attendants; }
    public void addAttendant(int attendantID) { attendants.add(attendantID); }
    public void delAttendant(int attendantID) { attendants.remove(attendantID); }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", subjectID=" + subjectID +
                ", teacherID=" + teacherID +
                ", attendants=" + attendants +
                '}';
    }
}
