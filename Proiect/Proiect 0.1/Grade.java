package com.company;

public class Grade {
    private static int nextID = 1;
    private final int gradeID, attendantID;
    private int grade;

    Grade(int grade, int attendantID) {
        gradeID = nextID++;
        this.attendantID = attendantID;
        this.grade = grade;
    }

    public int getGradeID() { return gradeID; }
    public int getAttendantID() { return attendantID; }
    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeID=" + gradeID +
                ", attendantID=" + attendantID +
                ", grade=" + grade +
                '}';
    }
}
