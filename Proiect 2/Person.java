package com.company;

public class Person {
    protected String fName, lName;
    Person() {
        this.fName = "";
        this.lName = "";
    }
    Person(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }
    public String getFName() { return fName; }
    public String getLName() { return lName; }
}
