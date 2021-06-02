package entities;

import database.Audit;
import database.AuditLevel;
import database.Database;
import graphics.GraphicInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    static String url = "jdbc:mysql://localhost:3306/db?user=root&password=password";
    public static void createTables(Database db) {
        db.addTable(Student.class);
        db.addTable(Subject.class);
        db.addTable(Teacher.class);
        db.addTable(Course.class);
        db.addTable(Attendant.class);
        db.addTable(Grade.class);
    }
    public static void loadTables(Database db) {
        db.loadTable(Student.class, "students");
        db.loadTable(Subject.class, "subjects");
        db.loadTable(Teacher.class, "teachers");
        db.loadTable(Course.class, "courses");
        db.loadTable(Attendant.class, "attendants");
        db.loadTable(Grade.class, "grades");
    }
    public static void populate(Database db) {
        int ids1 = db.insert(new Student("Iorghu", "Argetoianu"));
        int ids2 = db.insert(new Student("Luca", "Pirvu"));
        int ids3 = db.insert(new Student("Pereteanu", "Stanasila"));
        int ids4 = db.insert(new Student("Popescu", "Ion"));
        int ids5 = db.insert(new Student("Soare", "Dragomir"));
        int ids6 = db.insert(new Student("Mario", "Tatarescu"));
        int ids7 = db.insert(new Student("Vladimir", "Draghici"));

        int idj1 = db.insert(new Subject("Informatica"));
        int idj2 = db.insert(new Subject("Matematica"));
        int idj3 = db.insert(new Subject("Fizica"));
        int idj4 = db.insert(new Subject("Chimie"));
        int idj5 = db.insert(new Subject("Istorie"));

        int idt1 = db.insert(new Teacher("Costi", "Barbu"));   // mate, info
        int idt2 = db.insert(new Teacher("Danut", "Radacanu"));// chimie
        int idt3 = db.insert(new Teacher("Dan", "Cristescu")); // fizica
        int idt4 = db.insert(new Teacher("Mihaita", "Stoica"));// istorie
        int idt5 = db.insert(new Teacher("Popescu", "Ion"));   // mate

        int idc1 = db.insert(new Course(idj1, idt1));
        int idc2 = db.insert(new Course(idj2, idt1));
        int idc3 = db.insert(new Course(idj4, idt2));
        int idc4 = db.insert(new Course(idj3, idt3));
        int idc5 = db.insert(new Course(idj5, idt4));
        int idc6 = db.insert(new Course(idj1, idt5));

        int ida1 = db.insert(new Attendant(idc1, ids1));    // info1
        int ida2 = db.insert(new Attendant(idc1, ids2));    // info1
        int ida3 = db.insert(new Attendant(idc1, ids3));    // info1
        int ida4 = db.insert(new Attendant(idc1, ids4));    // info1
        int ida5 = db.insert(new Attendant(idc6, ids5));    // info2
        int ida6 = db.insert(new Attendant(idc6, ids6));    // info2
        int ida7 = db.insert(new Attendant(idc6, ids7));    // info2
        int ida8 = db.insert(new Attendant(idc2, ids1));    // mate
        int ida9 = db.insert(new Attendant(idc2, ids2));    // mate

        db.insert(new Grade(ida1, 10));
        db.insert(new Grade(ida2, 7));
        db.insert(new Grade(ida3, 9));
        db.insert(new Grade(ida4, 10));
        db.insert(new Grade(ida3, 8));
    }
    public static void _dropTables() {
        try {
            Connection con = DriverManager.getConnection(url);
            for(String tableName: new String[]{"Grades", "Attendants", "Courses", "Teachers", "Subjects", "Students"})
                try {
                    con.prepareStatement("drop table "+tableName+";").execute();
                    System.out.println("Table "+tableName+" dropped");
                } catch (Throwable e) {
                    System.out.println("Table "+tableName+" doesn't exist");
                }
        } catch (SQLException e) {
            System.err.println("Connection could not be established");
        }
    }
    public static void queries(Database db) {
        System.out.println("\nStudent's grades and the teacher which evaluated them:");
        db.allInfo(Grade.class).forEach(x -> System.out.printf("%s gave %s a %s\n",
                x.get("Teacher.lName"), x.get("Student.lName"), x.get("Grade.Grade")));
        System.out.println();

        System.out.println("\nSubjects attended by students:");
        db.allInfo(Attendant.class).forEach(x -> System.out.printf("%s attends a course of %s\n",
                x.get("Student.lName"), x.get("Subject.Subject_Name")));
        System.out.println();

        System.out.println("\nSubjects attended by all students which received grades:");
        db.allInfo(Grade.class).forEach(x -> System.out.printf("%s attends a course of %s with grades %s\n",
                x.get("Student.lName"), x.get("Subject.Subject_Name"),
                Arrays.toString(db.select(Grade.class, new HashMap<>() {{
                    put("AttendantID", y -> String.valueOf(y).equals(x.get("Attendant")));
                }}).stream().map(grade -> ((Grade) grade).getGrade()).toArray())));
        System.out.println();

        System.out.print("\nSubjects with attendants:");
        System.out.println(new HashSet<String>(){{
            db.allInfo(Attendant.class).forEach(x -> add(x.get("Subject.Subject_Name")));
        }});

        System.out.print("\nTeachers with students:");
        System.out.println(new HashSet<String>(){{
            db.allInfo(Attendant.class).forEach(x -> add(x.get("Teacher.fName")));
        }});

        System.out.println("\nProfessor teaches subjects:");
        db.allInfo(Course.class).forEach(x -> System.out.printf("%s teaches %s\n",
                x.get("Teacher.lName"), x.get("Subject.Subject_Name")));

        System.out.print("\nSubjects with grades:");
        System.out.println(new HashSet<String>(){{
            db.allInfo(Grade.class).forEach(x -> add(x.get("Subject.Subject_Name")));
        }});
    }
    public static void demo() {
        Database db = new Database(url);
        Audit.auditLevels.remove(AuditLevel.INSERT);

        System.out.println("\n\n---- Load ----");
        loadTables(db);
        System.out.println("\n\n---- Describe ----");
        db.describeTables();
        System.out.println("\n\n---- Show ----");
        db.showTables();
        System.out.println("\n\n---- Drop ----");
        db.dropTables();
        System.out.println("\n\n---- Create ----");
        createTables(db);
        System.out.println("\n\n---- Insert ----");
        populate(db);


        System.out.println("\n\n--- CRUD ---");
        int newID = db.insert(new Student("dummyStudentFName", "dummyStudentLName"));   // create
        db.update(Student.class, newID, "fName", "newName");                         // update
        System.out.println(db.select(Student.class, newID));                                         // read
        db.delete(Student.class, newID);                                                             // delete

        System.out.println("\n\n--- Queries ---");
        queries(db);
    }
    public static void main(String[] args) {
        GraphicInterface db = new GraphicInterface(url, "Catalogue");
        loadTables(db);
        db.run();
    }
}
