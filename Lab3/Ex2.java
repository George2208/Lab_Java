package com.company;

import java.util.Scanner;

public class Ex2 {
    public static void main(String[] args) {
        String str = new Scanner(System.in).nextLine();
        for(int i=0; i<str.length()/2; i++) {
            if(str.charAt(i) != str.charAt(str.length()-i-1)) {
                System.out.println(false);
                return;
            }
        }
        System.out.println(true);
    }
}
