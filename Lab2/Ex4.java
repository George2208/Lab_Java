package com.company;

class Singleton {
    private static Singleton instance = null;
    public String data;

    private Singleton() {
        System.out.println("constructor");
        data = "info";
    }

    public static Singleton getInstance() {
        if(instance == null)
            instance = new Singleton();
        return instance;
    }
}

public class Ex4 {
    public static void main(String[] args) {
        Singleton x = Singleton.getInstance();
        Singleton y = Singleton.getInstance();
    }
}
