package entities;

import database.Getter;
import database.Row;
import database.Setter;
import database.TableObject;

final public class Attendant extends Row {
    private int courseID, studentID;
    public Attendant(int courseID, int studentID) {
        super(0);
        this.courseID = courseID;
        this.studentID = studentID;
    }
    @TableObject(tableName = "Attendants") public Attendant(int id, String courseID, String studentID) {
        super(id);
        this.studentID = Integer.parseInt(studentID);
        this.courseID = Integer.parseInt(courseID);
    }
    @Getter(alias = "Course_ID", target = Course.class)
    public String getCourseID() { return String.valueOf(courseID); }
    @Getter(alias = "Student_ID", target = Student.class)
    public String getStudentID() { return String.valueOf(studentID); }
    @Setter(alias = "Course_ID")
    public void setCourseID(String courseID) { this.courseID = Integer.parseInt(courseID); }
    @Setter(alias = "Student_ID")
    public void setStudentID(String studentID) { this.studentID = Integer.parseInt(studentID); }
}
