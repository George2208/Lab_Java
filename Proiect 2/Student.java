package com.company;

import java.util.List;
import java.util.Map;

public class Student extends Person implements TableRow {
    private static int nextID = 1;
    private final int ID;
    public Student() { ID = 0; }
    public Student(String fName, String lName) {
        super(fName, lName);
        ID = nextID++;
    }

    @Override
    public List<String> getData() { return List.of(this.fName, this.lName); }
    @Override
    public <T extends TableRow> void addData(Map<Integer, T> map, String[] columns) throws IndexOutOfBoundsException {
        Student student = new Student(columns[0], columns[1]);
        map.put(student.ID, (T) student);
    }
    @Override
    public String toString() {
        return "Student{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", ID=" + ID +
                '}';
    }
}
