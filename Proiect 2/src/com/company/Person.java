package com.company;

public abstract class Person extends TableRow {
    protected String fName, lName;
    Person(int id, String fName, String lName) {
        super(id);
        this.fName = fName;
        this.lName = lName;
    }
    public String getFName() { return fName; }
    public String getLName() { return lName; }
}
