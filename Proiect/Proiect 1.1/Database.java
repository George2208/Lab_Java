package com.company;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Integer, Student> students = new HashMap<>();
    private final Map<Integer, Teacher> teachers = new HashMap<>();
    private final Map<Integer, Subject> subjects = new HashMap<>();
    private final Map<Integer, Course> courses = new HashMap<>();
    private final Map<Integer, Attendant> attendants = new HashMap<>();
    private final Map<Integer, Grade> grades = new HashMap<>();

    private void deleteAttendant(int attendantID) {
        for(int i : attendants.get(attendantID).getGrades()) { grades.remove(i); }
        attendants.remove(attendantID);
    }

    String[][] getStudentsData() {
        String[][] studentsData = new String[students.size()][5];
        int i = 0;
        for(Student student:students.values()) {
            studentsData[i][0] = String.valueOf(student.getStudentID());
            studentsData[i][1] = student.getFName();
            studentsData[i][2] = student.getLName();
            i++;
        }
        return studentsData;
    }
    String[][] getTeachersData() {
        String[][] teachersData = new String[teachers.size()][5];
        int i = 0;
        for(Teacher teacher:teachers.values()) {
            teachersData[i][0] = String.valueOf(teacher.getTeacherID());
            teachersData[i][1] = teacher.getFName();
            teachersData[i][2] = teacher.getLName();
            i++;
        }
        return teachersData;
    }

    public void add(Student obj) { students.put(obj.getStudentID(), obj); }
    public void add(Teacher obj) { teachers.put(obj.getTeacherID(), obj); }
    public void add(Subject obj) { subjects.put(obj.getSubjectID(), obj); }
    public void add(Course obj) {
        if(!teachers.containsKey(obj.getTeacherID())) { throw new RuntimeException("Invalid teacher id"); }
        if(!subjects.containsKey(obj.getSubjectID())) { throw new RuntimeException("Invalid subject id"); }
        if(!teachers.get(obj.getTeacherID()).getSubjects().contains(obj.getCourseID())) {
            throw new RuntimeException("This teacher cannot teach this course's subject");
        }
        courses.put(obj.getCourseID(), obj);
        teachers.get(obj.getTeacherID()).addCourse(obj.getCourseID());
        subjects.get(obj.getSubjectID()).addCourse(obj.getCourseID());
    }
    public void add(Grade obj) {
        if(!attendants.containsKey(obj.getAttendantID())) { throw new RuntimeException("Invalid attendant id"); }
        grades.put(obj.getGradeID(), obj);
        attendants.get(obj.getAttendantID()).addGrade(obj.getGradeID());
    }

    public void deleteGrade(int gradeID) {
        if(!grades.containsKey(gradeID)) { throw new RuntimeException("Invalid grade id"); }
        grades.remove(gradeID);
    }
    public void deleteCourse(int courseID) {
        for(int i : courses.get(courseID).getAttendants()) { deleteAttendant(i); }
        courses.remove(courseID);
    }
    public void deleteSubject(int subjectID) {
        for(int i : subjects.get(subjectID).getCourses()) { deleteCourse(i); }
        subjects.remove(subjectID);
    }
    public void deleteTeacher(int teacherID) {
        for(int i : teachers.get(teacherID).getCourses()) { deleteCourse(i); }
        teachers.remove(teacherID);
    }
    public void deleteStudent(int studentID) {
        for(int i : students.get(studentID).getAttendances()) { deleteAttendant(i); }
        students.remove(studentID);
    }

    String getStudentFName(int studentID) { return students.get(studentID).getFName(); }
    String getStudentLName(int studentID) { return students.get(studentID).getLName(); }
    void setStudentFName(int studentID, String fName) { students.get(studentID).setFName(fName); }
    void setStudentLName(int studentID, String lName) { students.get(studentID).setLName(lName); }
    void addStudentAttendance(int studentID, int courseID) {
        if(!courses.containsKey(courseID)) { throw new RuntimeException("Invalid course id"); }
        if(!students.containsKey(studentID)) { throw new RuntimeException("Invalid student id"); }
        Attendant attendant = new Attendant(studentID, courseID);
        attendants.put(attendant.getAttendantID(), attendant);
        students.get(studentID).addAttendance(courseID, attendant.getAttendantID());
        courses.get(courseID).addAttendant(attendant.getAttendantID());
    }
    void delStudentAttendance(int studentID, int courseID) {
        if(!courses.containsKey(courseID)) { throw new RuntimeException("Invalid course id"); }
        if(!students.containsKey(studentID)) { throw new RuntimeException("Invalid student id"); }
        int attendantID = students.get(studentID).getAttendantForCourse(courseID);
        for(int gradeID : attendants.get(attendantID).getGrades()) { grades.remove(gradeID); }
        courses.get(courseID).delAttendant(attendantID);
        students.get(studentID).delAttendance(attendantID);
        attendants.remove(attendantID);
    }





    void addTeacherSubject(int teacherID, int subjectID) {
        if(!teachers.containsKey(teacherID)) { throw new RuntimeException("Invalid teacher id"); }
        if(!subjects.containsKey(subjectID)) { throw new RuntimeException("Invalid subject id"); }
        teachers.get(teacherID).addSubject(subjectID);
    }
    Map<Integer, Float> queryStudentCourseAverage(int studentID) {
        if(!students.containsKey(studentID)) { throw new RuntimeException("This student does not exist"); }
        Map<Integer, Float> classAverage = new HashMap<>();
        for(int i:students.get(studentID).getAttendances()) {
            float s = 0;
            for(int j:attendants.get(i).getGrades()) { s += grades.get(j).getGrade(); }
            classAverage.put(attendants.get(i).getCourseID(), (s/Math.max(1, attendants.get(i).getGrades().size())));
        }
        return classAverage;
    }
    Map<Integer, Integer> queryStudentTeachersSubjects(int studentID) {
        if(!students.containsKey(studentID)) { throw new RuntimeException("This student does not exist"); }
        Map<Integer, Integer> teacherClass = new HashMap<>();
        for(int i:students.get(studentID).getAttendances()) {
            Course course = courses.get(attendants.get(i).getCourseID());
            teacherClass.put(course.getTeacherID(), course.getSubjectID());
        }
        return teacherClass;
    }


    void _populate() {
        this.add(new Student("student fName 1", "student lName 1"));
        this.add(new Student("student fName 2", "student lName 2"));

        this.add(new Teacher("teacher fName 1", "teacher lName 1"));
        this.add(new Teacher("teacher fName 1", "teacher lName 1"));

        this.add(new Subject("subject_1"));
        this.add(new Subject("subject_2"));

        this.addTeacherSubject(1, 1);
        this.addTeacherSubject(1, 2);
        this.addTeacherSubject(2, 2);

        this.add(new Course(1, 1));
        this.add(new Course(2, 2));

        this.addStudentAttendance(1, 1);
        this.addStudentAttendance(1, 2);
        this.addStudentAttendance(2, 2);

        this.add(new Grade(10, 1));
        this.add(new Grade(7, 1));
        this.add(new Grade(8, 2));
        this.add(new Grade(9, 1));
        this.add(new Grade(5, 2));
        this.add(new Grade(10, 3));
        this.add(new Grade(10, 3));
    }

    void demo() {
        System.out.println(students);
        System.out.println(attendants);
        System.out.println(courses);
        this.delStudentAttendance(1, 2);
        System.out.println("\n\n");
        System.out.println(students);
        System.out.println(attendants);
        System.out.println(courses);
        
//        try {
//            System.out.println(queryStudentCourseAverage(1));
//            System.out.println(queryStudentCourseAverage(3));
//        } catch (RuntimeException exception) { System.out.println(exception.getMessage()); }
//        System.out.println(queryStudentTeachersSubjects(1));
    }
}
