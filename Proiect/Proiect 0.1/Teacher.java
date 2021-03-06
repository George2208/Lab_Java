package com.company;

import java.util.HashSet;
import java.util.Set;

public class Teacher extends Person {
    private static int nextID = 1;
    private final int teacherID;
    private final Set<Integer> subjects = new HashSet<>();
    private final Set<Integer> courses = new HashSet<>();

    Teacher(String fName, String lName) {
        super(fName, lName);
        teacherID = nextID++;
    }

    public int getTeacherID() { return teacherID; }

    public Set<Integer> getSubjects() { return subjects; }
    public void addSubject(int subjectID) { subjects.add(subjectID); }
    public void delSubject(int subjectID) { subjects.remove(subjectID); }

    public Set<Integer> getCourses() { return courses; }
    public void addCourse(int courseID) { courses.add(courseID); }
    public void delCourse(int courseID) { courses.remove(courseID); }

    @Override
    public String toString() {
        return "Teacher{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", teacherID=" + teacherID +
                ", subjects=" + subjects +
                ", courses=" + courses +
                '}';
    }
}
