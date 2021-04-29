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
    public void setFName(String fName) { this.fName = fName; }
    public void setLName(String lName) { this.lName = lName; }
    @Override public String toString() { return super.toString().formatted("%s, fName="+fName+", lName="+lName); }
}
