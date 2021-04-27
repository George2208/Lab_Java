package com.company;

public class Main {
    public static void populate(Database db) {
        db.add(new Student("StudentFName1", "StudentLName1"));
        db.add(new Student("StudentFName2", "StudentLName2"));
        db.add(new Student("StudentFName3", "StudentLName3"));
        db.add(new Student("StudentFName4", "StudentLName4"));
        db.add(new Teacher("TeacherFName1", "TeacherLName1"));
        db.add(new Teacher("TeacherFName2", "TeacherLName2"));
        db.add(new Teacher("TeacherFName3", "TeacherLName3"));
        db.add(new Subject("Subject1"));
        db.add(new Subject("Subject2"));
        db.add(new Course(1, 1));
        db.add(new Course(2, 1));
        db.add(new Course(2, 2));
        db.add(new Attendant(1, 1));
        db.add(new Attendant(1, 2));
        db.add(new Attendant(1, 3));
        db.add(new Attendant(2, 1));
        db.add(new Attendant(3, 2));
        db.add(new Attendant(4, 3));
        db.add(new Grade(1, 7));
        db.add(new Grade(1, 8));
        db.add(new Grade(2, 8));
        db.add(new Grade(3, 8));
        db.add(new Grade(4, 5));
        db.add(new Grade(6, 10));
    }
    public static void main(String[] args) {
        Database db = new Database();
        populate(db);
        db.debug();
        db.delete(Teacher.class, 1);
        db.debug();
        db.save("database.txt");
    }
}
