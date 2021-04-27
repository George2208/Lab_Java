package com.company;

class Pair<T1, T2> {
    public T1 fst;
    public T2 snd;
    public Pair(T1 fst, T2 snd) {
        this.fst = fst;
        this.snd = snd;
    }
    @Override public String toString() { return "Pair{fst=" + fst + ", snd=" + snd + '}'; }
}
