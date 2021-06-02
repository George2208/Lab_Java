package entities;

import database.Getter;
import database.Row;
import database.Setter;
import database.TableObject;

public class Grade extends Row {
    private int grade, attendantID;
    public Grade(int attendantID, int grade) {
        super(0);
        this.grade = grade;
        this.attendantID = attendantID;
    }
    @TableObject(tableName = "Grades") public Grade(int id, String attendantID, String grade) {
        super(id);
        this.grade = Integer.parseInt(grade);
        this.attendantID = Integer.parseInt(attendantID);
    }
    @Getter(alias = "Grade")
    public String getGrade() { return String.valueOf(grade); }
    @Getter(alias = "AttendantID", target = Attendant.class)
    public String getAttendantID() { return String.valueOf(attendantID); }
    @Setter(alias = "Grade")
    public void setGrade(String grade) { this.grade = Integer.parseInt(grade); }
    @Setter(alias = "AttendantID")
    public void setAttendantID(String attendantID) { this.attendantID = Integer.parseInt(attendantID); }
}
