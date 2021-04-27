package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Audit {
    public static String fileName = "logs";

    public static void entry(String message) {
        try {
            FileWriter file = new FileWriter(fileName, true);
            String time = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss   ").format(LocalDateTime.now());
            file.write(time + message + '\n');
            file.flush();
            file.close();
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
