package entities;

import database.TableObject;

final public class Student extends Person {
    public Student(String fName, String lName) { super(0, fName, lName); }
    @TableObject(tableName = "Students") public Student(int id, String fName, String lName) { super(id, fName, lName); }
}
