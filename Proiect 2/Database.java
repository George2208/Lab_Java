package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Database {
    Map<Integer, Student> studentMap = new HashMap<>();

    public void saveStudents(String fileName) {Database.save(studentMap, fileName); }
    public void loadStudents(String fileName) {Database.load(studentMap, fileName, Student.class);}

    public static <T extends TableRow> void save (Map<Integer, T> map, String fileName) {
        try {
            FileWriter file = new FileWriter(fileName);
            for (T row: map.values())
                file.write(String.join(",", row.getData())+'\n');
            file.flush();
            file.close();
        }
        catch (IOException e) { System.out.println(e.getMessage()); }
    }
    private static <T extends TableRow> void load (Map<Integer, T> map, String fileName, Class<T> tClass) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            T aux = tClass.getDeclaredConstructor().newInstance();
            while (scanner.hasNextLine()) { aux.addData(map, scanner.nextLine().split(",")); }
        }
        catch (FileNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }
}
