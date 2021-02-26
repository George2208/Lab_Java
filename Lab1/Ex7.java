package com.company;

import java.util.Arrays;

public class Ex7 {
    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt(), rez = 1;
        if(n < 1) {
            System.out.println("Invalid index");
            return;
        }
        for(int x = 1, y = 1; n > 2; n--, rez = x + y, x = y, y = rez);
        System.out.println(rez);
    }
}
