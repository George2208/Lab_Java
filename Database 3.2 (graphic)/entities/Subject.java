package entities;

import database.Getter;
import database.Row;
import database.Setter;
import database.TableObject;

final public class Subject extends Row {
    private String name;
    public Subject(String name) {
        super(0);
        this.name = name;
    }
    @TableObject(tableName = "Subjects") public Subject(int id, String name) {
        super(id);
        this.name = name;
    }
    @Getter(alias = "Subject_Name") public String getName() { return name; }
    @Setter(alias = "Subject_Name") public void setName(String name) { this.name = name; }
}
