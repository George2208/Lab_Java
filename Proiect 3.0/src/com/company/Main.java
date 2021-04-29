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
    public static void demoAddTable(Database db) {
        class DummyClass1 extends TableRow { @ID public DummyClass1() {super(0);} }
        class DummyClass2 extends TableRow { @ID public DummyClass2(String s) {super(0);} }
        class DummyClass3 extends TableRow { @ID public DummyClass3(int i) {super(0);} }
        class DummyClass4 extends TableRow { @ID public DummyClass4(String s, int i) {super(0);} }
        db.addTable("-DummyClass1", DummyClass1.class); // works
        db.addTable("-DummyClass2", DummyClass2.class); // works
        db.addTable("-DummyClass3", DummyClass3.class); // err
        db.addTable("-DummyClass4", DummyClass4.class); // err
        System.out.println("\nTable after addTable:");
        db.print();
        db.deleteTable("-DummyClass1");
        db.deleteTable("-DummyClass2");
        System.out.println("\nTable after deleteTable:");
        db.print();
    }
    public static void demoQuery(Database db) {
        Query query = new Query(db);
        System.out.println("\nAverage grade per student");
        query.averageGradePerStudent();
        System.out.println("\nCourses attended by student");
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
        demoAddTable(db);
        demoQuery(db);
    }
}
