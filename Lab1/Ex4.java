package com.company;

import java.util.Scanner;

public class Ex4 {
    static int multiplies(int n) {
        return (n/3)*(n - n % 3 + 3) / 2 + (n/5)*(n - n % 5 + 5) / 2 - (n/15)*(n - n % 15 + 15) / 2;
    }
    public static void main(String[] args) {
        System.out.println(multiplies(new Scanner(System.in).nextInt()));
    }
}
