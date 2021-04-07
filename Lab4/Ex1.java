package com.company;

import java.util.Random;

interface Task {
    public void execute();
}

class OutTask implements Task {
    private final String message;
    OutTask(String message) { this.message = message; }
    @Override
    public void execute() { System.out.println(message); }
}

class RandomTask implements Task {
    private final int message;
    RandomTask() { this.message = new Random().nextInt(); }
    @Override
    public void execute() { System.out.println(message); }
}

class CounterOutTask implements Task {
    private static int counter = 0;
    @Override
    public void execute() { System.out.println(counter++); }
}

public class Ex1 {
    public static void main(String[] args) {
        new OutTask("message").execute();
        new RandomTask().execute();
        new CounterOutTask().execute();
        new CounterOutTask().execute();
    }
}