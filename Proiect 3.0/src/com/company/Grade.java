package com.company;

public class Grade extends TableRow{
    private static int nextID = 1;
    private int grade;
    public Grade(int attendantID, int grade) {
        super(nextID++);
        this.grade = grade;
        this.hasDependency.put(Attendant.class, attendantID);
    }
    @ID public Grade(String newID, String attendantID, String grade) {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.grade = Integer.parseInt(grade);
        this.hasDependency.put(Attendant.class, Integer.parseInt(attendantID));
    }
    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }
    @Override public String toString() { return super.toString().formatted(", grade="+grade); }
}
