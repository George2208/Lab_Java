package com.company;

import java.util.Scanner;

public class Ex1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; i += 2)
            System.out.print(String.valueOf(i) + ", ");
    }
}
