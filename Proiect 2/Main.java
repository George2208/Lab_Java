package com.company;

public class Main {

    public static void main(String[] args) {
        Database db = new Database();
        db.loadStudents("students");
        System.out.println(db.studentMap);
        db.saveStudents("teachers");
    }
}
