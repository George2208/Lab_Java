package entities;

import database.Getter;
import database.Row;
import database.Setter;
import database.TableObject;

public class Course extends Row {
    private int subjectID, teacherID;
    public Course(int subjectID, int teacherID) {
        super(0);
        this.subjectID = subjectID;
        this.teacherID = teacherID;
    }
    @TableObject(tableName = "Courses") public Course(int id, String subjectID, String teacherID) {
        super(id);
        this.subjectID = Integer.parseInt(subjectID);
        this.teacherID = Integer.parseInt(teacherID);
    }
    @Getter(alias = "SubjectID", target = Subject.class)
    public String getSubjectID() { return String.valueOf(subjectID); }
    @Getter(alias = "TeacherID", target = Teacher.class)
    public String getTeacherID() { return String.valueOf(teacherID); }
    @Setter(alias = "SubjectID")
    public void setSubjectID(String subjectID) { this.subjectID = Integer.parseInt(subjectID); }
    @Setter(alias = "TeacherID")
    public void setTeacherID(String teacherID) { this.teacherID = Integer.parseInt(teacherID); }
}
