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
    public static void demoQuery(Database db) {
        Query query = new Query(db);
        System.out.println("Average grade per student");
        query.averageGradePerStudent();
        System.out.println("Courses attended by student");
        query.studentSubjects();
    }
    public static void main(String[] args) {
        Audit.auditLevels.remove(AuditLevel.ADD_ROW);
        Database db = new Database() {{
            addTable("-Students", Student.class);
            addTable("-Teachers", Teacher.class);
            addTable("-Subjects", Subject.class);
            addTable("-Courses", Course.class);
            addTable("-Attendants", Attendant.class);
            addTable("-Grades", Grade.class);
        }};
        populate(db);
        db.print();

        System.out.println("\n\n\n");
        demoQuery(db);
        System.out.println("\n\n\n");

        db.save();

        System.out.println("Cascade delete student 1:");
        db.delete(Student.class, 1);
        System.out.println("\n\n\n");
        db.print();
    }
}
