package com.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Teacher extends Person {
    private static int nextID = 1;
    final Set<Integer> courses = new HashSet<>();
    public Teacher(String fName, String lName) { super(nextID++, fName, lName); }
    @ID public Teacher(String newID, String fName, String lName) {
        super(Integer.parseInt(newID), fName, lName);
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
    }

    void addCourse(int courseID) { courses.add(courseID); }
    public Set<Integer> getCourses () { return courses; }

    @Override public List<String> getData() { return List.of(String.valueOf(this.id), this.fName, this.lName); }
    @Override Collection<Pair<Class<? extends TableRow>, Collection<Integer>>> isDependency() {
        return new HashSet<>(){{
            add(new Pair<>(Course.class, courses));
        }};
    }
    @Override Collection<Pair<Class<? extends TableRow>, Integer>> hasDependency() {
        return new HashSet<>();
    }
    @Override public String toString() {
        return "Teacher{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", id=" + id +
                ", courses=" + courses +
                '}';
    }
}
