package com.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Course extends TableRow {
    private static int nextID = 1;
    private final int teacherID, subjectID;
    private final Set<Integer> attendances = new HashSet<>();
    public Course(int teacherID, int subjectID) {
        super(nextID++);
        this.subjectID = subjectID;
        this.teacherID = teacherID;
    }
    @ID public Course(String newID, String teacherID, String subjectID) {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.subjectID = Integer.parseInt(subjectID);
        this.teacherID = Integer.parseInt(teacherID);
    }

    public int getTeacherID() { return teacherID; }
    public int getSubjectID() { return subjectID; }
    public void addAttendance (int attendantID) { attendances.add(attendantID); }

    @Override public List<String> getData() { return List.of(String.valueOf(this.id), String.valueOf(this.teacherID), String.valueOf(this.subjectID)); }
    @Override Collection<Pair<Class<? extends TableRow>, Collection<Integer>>> isDependency() {
        return new HashSet<>(){{
            add(new Pair<>(Attendant.class, attendances));
        }};
    }
    @Override Collection<Pair<Class<? extends TableRow>, Integer>> hasDependency() {
        return new HashSet<>(){{
            add(new Pair<>(Teacher.class, teacherID));
            add(new Pair<>(Subject.class, subjectID));
        }};
    }
    @Override public String toString() {
        return "Course{" +
                "id=" + id +
                ", teacherID=" + teacherID +
                ", subjectID=" + subjectID +
                ", attendances=" + attendances +
                '}';
    }
}
