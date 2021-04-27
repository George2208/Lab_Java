package com.company;

public class Query extends Database {
    Query (Database db) {
        classes = db.classes;
        maps = db.maps;
        constructors = db.constructors;
        priority = db.priority;
    }

    void averageGradePerStudent() {
        if (!maps.containsKey(Student.class) || !maps.containsKey(Attendant.class) || !maps.containsKey(Grade.class)) {
            System.err.println("Tables missing");
            return;
        }
        for (TableRow student: maps.get(Student.class).values()) {
            int sum = 0, count = 0;
            for (int attendantID: student.isDependency().get(Attendant.class)) {
                for (int gradeID: maps.get(Attendant.class).get(attendantID).isDependency().get(Grade.class)) {
                    sum += ((Grade) maps.get(Grade.class).get(gradeID)).getGrade();
                    count++;
                }
            }
            if (count==0) {
                System.out.println("  "+((Student) student).getFName()+" doesn't have any grades");
            } else {
                System.out.println("  "+((Student) student).getFName()+" has average "+(sum/count));
            }
        }
    }

    void studentSubjects() {
        if (!maps.containsKey(Student.class) || !maps.containsKey(Attendant.class) || !maps.containsKey(Course.class)) {
            System.err.println("Tables missing");
            return;
        }
        for (TableRow student: maps.get(Student.class).values()) {
            System.out.println("  "+((Student) student).getFName()+" attends to:");
            for (int attendantID: student.isDependency().get(Attendant.class)) {
                System.out.println("    "+((Subject) maps.get(Subject.class).get(maps.get(Course.class).get(maps.get(Attendant.class).get(attendantID).hasDependency().get(Course.class)).hasDependency().get(Subject.class))).getName());
            }
        }
    }
}
