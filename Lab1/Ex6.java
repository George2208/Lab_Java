package com.company;

import java.util.Scanner;

public class Ex6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int s = 0, n = 0;
        while(true) {
            int aux = scanner.nextInt();
            if(aux == -1)
                break;
            s += aux;
            n++;
        }
        n = Math.max(n, 1);
        System.out.println((float)s/n);
    }
}
