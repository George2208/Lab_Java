package com.company;

import java.util.HashMap;
import java.util.Map;

public class Database {
    Map<Integer, Student> Students = new HashMap<>();
    Map<Integer, Teacher> Teachers = new HashMap<>();
    Map<Integer, Subject> Subjects = new HashMap<>();
    Map<Integer, Course> Courses = new HashMap<>();
    Map<Integer, Attendant> Attendants = new HashMap<>();
    Map<Integer, Grade> Grades = new HashMap<>();

    void add(Student obj) { Students.put(obj.getStudentID(), obj); }
    void add(Subject obj) { Subjects.put(obj.getSubjectID(), obj); }
    void add(Teacher obj) { Teachers.put(obj.getTeacherID(), obj); }
    void add(Course obj) {
        if(!Teachers.containsKey(obj.getTeacherID())) {
            System.err.println("Invalid teacher id");
            return;
        }
        if(!Subjects.containsKey(obj.getSubjectID())) {
            System.err.println("Invalid subject id");
            return;
        }
        if(!Teachers.get(obj.getTeacherID()).getSubjects().contains(obj.getCourseID())) {
            System.err.println("This teacher cannot teach this course's subject");
        }
        Courses.put(obj.getCourseID(), obj);
        Teachers.get(obj.getTeacherID()).addCourse(obj.getCourseID());
        Subjects.get(obj.getSubjectID()).addCourse(obj.getCourseID());
    }
    void addTeacherSubject(int teacherID, int subjectID) {
        if(!Teachers.containsKey(teacherID)) {
            System.err.println("Invalid teacher id");
            return;
        }
        if(!Subjects.containsKey(subjectID)) {
            System.err.println("Invalid subject id");
            return;
        }
        Teachers.get(teacherID).addSubject(subjectID);
        Subjects.get(subjectID).addTeacher(teacherID);
    }
    void add(Attendant obj) {
        Attendants.put(obj.getAttenantID(), obj);
        Students.get(obj.getStudentID()).addAttendance(obj.getAttenantID());
        Courses.get(obj.getCourseID()).addAttendant(obj.getAttenantID());
    }
    void add(Grade obj) {
        Grades.put(obj.getGradeID(), obj);
        Attendants.get(obj.getAttendantID()).addGrade(obj.getGradeID());
    }

    void querryStudentSubjects() {

    }

    void demo() {
        // populate
        this.add(new Student("student fname 1", "student lname 1"));
        this.add(new Student("student fname 2", "student lname 2"));

        this.add(new Teacher("teacher fname 1", "teacher lname 1"));
        this.add(new Teacher("teacher fname 1", "teacher lname 1"));

        this.add(new Subject("subject 1"));
        this.add(new Subject("subject 2"));

        this.addTeacherSubject(1, 1);
        this.addTeacherSubject(1, 2);
        this.addTeacherSubject(2, 2);

        this.add(new Course(1, 1));
        this.add(new Course(2, 2));

        this.add(new Attendant(1, 1));
        this.add(new Attendant(1, 2));
        this.add(new Attendant(2, 2));

        this.add(new Grade(10, 1));


        System.out.println(this.Students);
        System.out.println(this.Teachers);
        System.out.println(this.Subjects);
        System.out.println(this.Courses);
        System.out.println(this.Attendants);
        System.out.println(this.Grades);

        querryStudentSubjects();
    }
}
