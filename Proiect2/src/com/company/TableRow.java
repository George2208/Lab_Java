package com.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class TableRow {
    protected final int id;
    protected TableRow(int id) { this.id = id; }
    public int id() { return id; }
    abstract List<String> getData();
    abstract Collection<Pair<Class<? extends TableRow>, Collection<Integer>>> isDependency(); //{ return new HashSet<>(); }
    abstract Collection<Pair<Class<? extends TableRow>, Integer>> hasDependency(); //{ return new HashSet<>(); }
}
