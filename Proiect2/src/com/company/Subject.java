package com.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subject extends TableRow {
    private static int nextID = 1;
    private String name;
    final Set<Integer> courses = new HashSet<>();
    public Subject(String name) {
        super(nextID++);
        this.name = name;
    }
    @ID public Subject(String newID, String name) throws RuntimeException {
        super(Integer.parseInt(newID));
        if (id < nextID) { throw new RuntimeException("Invalid id"); }
        nextID = id+1;
        this.name = name;
    }

    void addCourse(int courseID) { courses.add(courseID); }
    public Set<Integer> getCourses () { return courses; }

    @Override public List<String> getData() { return List.of(String.valueOf(this.id), this.name); }
    @Override public Collection<Pair<Class<? extends TableRow>, Collection<Integer>>> isDependency() {
        return new HashSet<>(){{
            add(new Pair<>(Course.class, courses));
        }};
    }
    @Override public void removeDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Course.class) {
            courses.remove(id);
        }
    }
    @Override public void addDependency(Class<? extends TableRow> cls, Integer id){
        if (cls == Course.class) {
            courses.add(id);
        }
    }
    @Override public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
