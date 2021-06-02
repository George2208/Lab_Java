package entities;

import database.TableObject;

final public class Teacher extends Person {
    public Teacher(String fName, String lName) { super(0, fName, lName); }
    @TableObject(tableName = "Teachers") public Teacher(int id, String fName, String lName) { super(id, fName, lName); }
}
