package entities;

import database.Getter;
import database.Row;
import database.Setter;

public abstract class Person extends Row {
    private String fName, lName;
    Person(int id, String fName, String lName) {
        super(id);
        this.fName = fName;
        this.lName = lName;
    }
    @Getter(alias = "fName") public String getFName() { return fName; }
    @Getter(alias = "lName") public String getLName() { return lName; }
    @Setter(alias = "fName") public void setFName(String fName) { this.fName = fName; }
    @Setter(alias = "lName") public void setLName(String lName) { this.lName = lName; }
}
