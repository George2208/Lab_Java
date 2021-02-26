package com.company;

import java.util.Scanner;

public class Ex3 {
    public static void main(String[] args) {
        int f = 1, n = new Scanner(System.in).nextInt();
        if(n < 0) {
            System.out.println("Nu se poate calcula factorialul");
            return;
        }
        for (int i = n; i > 1; i--)
            f *= i;
        System.out.println(f);
    }
}
