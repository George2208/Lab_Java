package com.company;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        db.load("data");
        db.showTables();
        System.out.println();
//        db.delete(Attendant.class, 1);
        System.out.println();
        db.showTables();
        db.save();
    }
}
