package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Ex3 {
    public static void main(String[] args) {
        String s1 = new Scanner(System.in).nextLine();
        String s2 = new Scanner(System.in).nextLine();
        Map<Character, Integer> dict1 = new HashMap<>();
        Map<Character, Integer> dict2 = new HashMap<>();
        for(Character i : s1.toCharArray()) {
            if(dict1.containsKey(i)) {
                dict1.put(i, dict1.get(i)+1);
            } else {
                dict1.put(i, 1);
            }
        }
        for(Character i : s2.toCharArray()) {
            if(dict2.containsKey(i)) {
                dict2.put(i, dict2.get(i)+1);
            } else {
                dict2.put(i, 1);
            }
        }
        System.out.println(dict1.equals(dict2));
    }
}
