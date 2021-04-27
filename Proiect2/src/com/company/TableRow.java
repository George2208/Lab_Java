package com.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class TableRow {
    protected final int id;
    protected TableRow(int id) { this.id = id; }
    public int id() { return id; }
    abstract List<String> getData();
    public Collection<Pair<Class<? extends TableRow>, Collection<Integer>>> isDependency() { return new HashSet<>(); }
    public Collection<Pair<Class<? extends TableRow>, Integer>> hasDependency() { return new HashSet<>(); }
    public void removeDependency(Class<? extends TableRow> cls, Integer id) {}
    public void addDependency(Class<? extends TableRow> cls, Integer id) {}
}
