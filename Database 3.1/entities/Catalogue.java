package entities;

import database.Database;
import database.Row;

import java.util.*;

public class Catalogue extends Database {
    public Catalogue(String url) { super(url); }
    public void StudentsGrades() {
        Map<Integer, List<Row>> studentID_Attendant = _join(Student.class, Attendant.class);
        Map<Integer, List<Row>> attendantID_Grade = _join(Attendant.class, Grade.class);
        for(int studentID: studentID_Attendant.keySet()) {
            List<Integer> grades = new ArrayList<>();
            for(Row attendant: studentID_Attendant.get(studentID))
                if(attendantID_Grade.containsKey(attendant.id))
                    for(Row grade: attendantID_Grade.get(attendant.id))
                        grades.add(Integer.parseInt(((Grade) grade).getGrade()));
            Student student = (Student) tables.get(Student.class).rows.get(studentID);
            System.out.printf("%-20s %s\n", student.getFName()+' '+student.getLName(), grades);
        }
    }
    public void nrAttendancesPerStudent() {
        Map<Integer, List<Row>> studentID_Attendant = _join(Student.class, Attendant.class);
        for(int studentID: studentID_Attendant.keySet()) {
            Student student = (Student) tables.get(Student.class).rows.get(studentID);
            System.out.printf("%s attends to %s courses.\n", student.getFName()+' '+student.getLName(), studentID_Attendant.get(studentID).size());
        }
    }
    public void nrCoursesPerTeacher() {
        Map<Integer, List<Row>> teacherID_Courses = _join(Teacher.class, Course.class);
        for(int teacherID: teacherID_Courses.keySet()) {
            Teacher teacher = (Teacher) tables.get(Teacher.class).rows.get(teacherID);
            System.out.printf("%s teaches %s courses.\n", teacher.getFName()+' '+teacher.getLName(), teacherID_Courses.get(teacherID).size());
        }
    }










}
