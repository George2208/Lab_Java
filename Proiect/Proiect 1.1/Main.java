package com.company;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        db._populate();
        App app = new App(db);
    }
}
