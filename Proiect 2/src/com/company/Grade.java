package com.company;

import java.util.*;

public class Grade extends TableRow{
    private static int nextID = 1;
    private final int attendantID;
    private final int grade;
    public Grade(int attendantID, int grade) {
        super(nextID++);
        this.attendantID = attendantID;
        this.grade = grade;
    }
    @ID public Grade(String newID, String attendantID, String grade) {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.attendantID = Integer.parseInt(attendantID);
        this.grade = Integer.parseInt(grade);
    }
    public int getGrade() { return grade; }
    @Override public List<String> getData() {
        return List.of(String.valueOf(this.id), String.valueOf(this.attendantID), String.valueOf(this.grade));
    }
    @Override public Map<Class<? extends TableRow>, Integer> hasDependency() {
        return new HashMap<>(){{ put(Attendant.class, attendantID); }};
    }
    @Override public String toString() {
        return "Grade{" +
                "id=" + id +
                ", attendantID=" + attendantID +
                ", grade=" + grade +
                '}';
    }
}
