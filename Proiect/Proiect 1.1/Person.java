package com.company;

public abstract class Person {
    protected String fName, lName;

    Person(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }
    public String getFName() { return fName; }
    public String getLName() { return lName; }
    public void setFName(String fName) { this.fName = fName; }
    public void setLName(String lName) { this.lName = lName; }
}
