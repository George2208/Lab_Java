package com.company;

import java.util.*;

public abstract class TableRow {
    protected final int id;
    protected TableRow(int id) { this.id = id; }
    public int id() { return id; }
    abstract List<String> getData();
    public Map<Class<? extends TableRow>, Collection<Integer>> isDependency() { return new HashMap<>(); }
    public Map<Class<? extends TableRow>, Integer> hasDependency() { return new HashMap<>(); }
    public void removeDependency(Class<? extends TableRow> cls, Integer id) {}
    public void addDependency(Class<? extends TableRow> cls, Integer id) {}
}
