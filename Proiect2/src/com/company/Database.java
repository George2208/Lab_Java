package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

class Pair<T1, T2> {
    public T1 fst;
    public T2 snd;
    public Pair(T1 fst, T2 snd) {
        this.fst = fst;
        this.snd = snd;
    }
}

public class Database {
    static class PairListClass<T> extends Pair<List<T>, Class<T>> {
        public PairListClass(List<T> list, Class<T> cls) {
            super(list, cls);
        }
    }
    private final Map<Integer, Student> studentMap = new HashMap<>();
    private final Map<Integer, Teacher> teacherMap = new HashMap<>();
    private final Map<Integer, Subject> subjectMap = new HashMap<>();
    private final Map<Integer, Course> courseMap = new HashMap<>();
    private final Map<Integer, Attendant> attendantMap = new HashMap<>();
    private final Map<Integer, Grade> gradeMap = new HashMap<>();

    private final Map<String, Class<? extends TableRow>> classes = new HashMap<>() {{
        put("-Students", Student.class);
        put("-Teachers", Teacher.class);
        put("-Subjects", Subject.class);
        put("-Courses", Course.class);
        put("-Attendants", Attendant.class);
        put("-Grades", Grade.class);
    }};
    Map<Class<? extends TableRow>, Map<Integer, ? extends TableRow>> maps = new HashMap<>() {{
        put(Student.class, studentMap);
        put(Teacher.class, teacherMap);
        put(Subject.class, subjectMap);
        put(Course.class, courseMap);
        put(Attendant.class, attendantMap);
        put(Grade.class, gradeMap);
    }};

    public <T extends TableRow> void add(T row) {
//        if (row.getClass() == Student.class) {
//            studentMap.put(row.id(), (Student) row);
//            return;
//        }
//        if (row.getClass() == Teacher.class) {
//            teacherMap.put(row.id(), (Teacher) row);
//            return;
//        }
//        if (row.getClass() == Subject.class) {
//            subjectMap.put(row.id(), (Subject) row);
//            return;
//        }
//        if (row.getClass() == Course.class) {
//            Course course = (Course) row;
//            if (teacherMap.containsKey(course.getTeacherID()) && subjectMap.containsKey(course.getSubjectID())) {
//                teacherMap.get(course.getTeacherID()).addCourse(row.id());
//                subjectMap.get(course.getSubjectID()).addCourse(row.id());
//                courseMap.put(row.id(), course);
//            }
//            else { System.err.println("Invalid course " + course); }
//            return;
//        }
//        if (row.getClass() == Attendant.class) {
//            Attendant attendant = (Attendant) row;
//            if (studentMap.containsKey(attendant.getStudentID()) && courseMap.containsKey(attendant.getCourseID())) {
//                studentMap.get(attendant.getStudentID()).addAttendance(row.id());
//                courseMap.get(attendant.getCourseID()).addAttendance(row.id());
//                attendantMap.put(row.id(), attendant);
//            }
//            else { System.err.println("Invalid attendant " + attendant); }
//            return;
//        }
//        if (row.getClass() == Grade.class) {
//            Grade grade = (Grade) row;
//            if (attendantMap.containsKey(grade.getAttendantID())) {
//                attendantMap.get(grade.getAttendantID()).addGrade(row.id());
//                gradeMap.put(row.id(), grade);
//            }
//            else { System.err.println("Invalid grade " + grade); }
//            return;
//        }
//        System.out.println("Class " + row.getClass().getSimpleName() + " doesn't have 'add' implemented");

        Class<?> cls = row.getClass();
        int id = row.id();
        System.out.println("Adding "+cls.getSimpleName()+ " "+id);
        for (Pair<Class<? extends TableRow>, Integer> dependency: row.hasDependency()) {
            if (!maps.get(dependency.fst).containsKey(dependency.snd)) {
                System.out.println("    Invalid dependency for "+cls.getSimpleName()+ " " +id+" ("+dependency.fst.getSimpleName()+" "+dependency.snd+")");
                return;
            }
        }
        maps.get(row.getClass()).put(row.id(), row.getClass().cast(row));
    }
    public <T extends TableRow> void delete(Class<? extends TableRow> cls, Integer id) {
        System.out.println("Delete for "+cls.getSimpleName()+ " with id "+id);
        for (Pair<Class<? extends TableRow>, Integer> dependency: maps.get(cls).get(id).hasDependency()) {
            delete(dependency.fst, dependency.snd);
        }
        for (Pair<Class<? extends TableRow>, Collection<Integer>> dependency: maps.get(cls).get(id).isDependency()) {
            for (int dependencyID: dependency.snd) {
                delete(dependency.fst, dependencyID);
            }
        }
        if (!maps.get(cls).containsKey(id))
            System.out.println("No object found while trying to delete. Circular dependency??");
        maps.get(cls).remove(id);
        System.out.println("Deleted "+cls.getSimpleName()+ " " +id);
    }

    public static <T extends TableRow> void loadAux(String row, PairListClass<T> pair) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Constructor<?> i: pair.snd.getConstructors())
            for (Annotation annotation: i.getDeclaredAnnotations())
                if (annotation.annotationType().equals(ID.class)) {
                    Object obj = i.newInstance((Object[]) Arrays.stream(row.split(",")).map(String::strip).toArray(String[]::new));
                    pair.fst.add(pair.snd.cast(obj));
                    return;
                }
        System.err.println("No constructor found");
    }
    public void load (String fileName) {
        Map<String, PairListClass<? extends TableRow>> flags= new HashMap<>() {{
            for (String name: classes.keySet()) {
                put(name, new PairListClass<>(new ArrayList<>(), classes.get(name)));
            }
        }};
        try {
            Scanner file = new Scanner(new File(fileName));
            String currentTable = "";
            while (file.hasNextLine()) {
                String line = file.nextLine().strip();
                if (!line.equals("")) {
                    if(flags.containsKey(line)) { currentTable = line; }
                    else {
                        try { loadAux(line, flags.get(currentTable)); }
                        catch (NullPointerException e) { System.err.println("Invalid flag"); }
                        catch (IndexOutOfBoundsException e) { System.err.println("Invalid number of arguments for "+flags.get(currentTable).snd.getSimpleName()+ " constructor"); }
                        catch (NumberFormatException e) { System.err.println("Invalid id (NAN)"); }
                        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) { System.err.println("!!!Cast error!!! "+e.getMessage()); }
                        catch (RuntimeException e) { System.err.println(e.getMessage()); }
                    }
                }
            }
        }
        catch (FileNotFoundException e) { System.out.println("File not found"); }
        for (String key: new String[]{"-Students", "-Teachers", "-Subjects", "-Courses", "-Attendants", "-Grades"}) {
            for (Object obj: flags.get(key).fst) {     // TODO: Sort list
                add(flags.get(key).snd.cast(obj));
            }
        }
    }
    public void save () {
        for (String flag: classes.keySet()) {
            try {
                FileWriter file = new FileWriter(classes.get(flag).getSimpleName()+"s.csv");
                file.write(flag+'\n');
//                System.out.println((flag+'\n'));
                for (TableRow row: maps.get(classes.get(flag)).values()) {
                    file.write(String.join(",", row.getData())+'\n');
//                    System.out.println((String.join(",", row.getData())+'\n'));
                }
                file.flush();
                file.close();
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void showTables () {
        for (Map<Integer, ?> map: maps.values()) {
            System.out.println(map);
        }
    }
}
