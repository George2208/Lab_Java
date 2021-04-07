package com.company;

import java.util.Random;

class PasswordMaker {
    private static PasswordMaker instance = null;
    private static final int MAGIC_NUMBER = 10;
    private final String MAGIC_STRING = randomString(20);
    private final String name;

    private PasswordMaker(String name) { this.name = name; }

    public String getPassword () {
        return randomString(MAGIC_NUMBER) + MAGIC_STRING.substring(0, 10) +
                name.length() + new Random().nextInt(100);
    }

    public static PasswordMaker getInstance() {
        if(instance == null) { instance = new PasswordMaker("string"); }
        return instance;
    }

    private String randomString(int len){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

}

public class Ex4 {

    public static void main(String[] args) {
        System.out.println(PasswordMaker.getInstance().getPassword());

    }
}
