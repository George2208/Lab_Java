package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ex5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> even = new ArrayList<Integer>(), odd = new ArrayList<Integer>();
        for(int i = scanner.nextInt(); i > 0; i--) {
            int aux = scanner.nextInt();
            if((aux & 1) == 1)
                odd.add(aux);
            else
                even.add(aux);
        }
        System.out.print("Even: ");
        System.out.print(even);
        System.out.print("\nOdd:  ");
        System.out.print(odd);
    }
}
