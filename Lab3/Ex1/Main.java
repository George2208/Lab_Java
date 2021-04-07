package com.company;

public class Main {
    public static void main(String[] args) {
        CandyBox c1 = new Merci("-", "-", 5, 5, 5);
        CandyBox c2 = new Lindt("-", "-", 3, 5);
        CandyBox c3 = new Milka("-", "-", 5);
        System.out.println(c1);
        System.out.println(c3);
        System.out.println(c1.equals(c3));
    }
}
