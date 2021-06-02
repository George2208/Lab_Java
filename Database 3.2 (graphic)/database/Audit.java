package database;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

public class Audit {
    public static String fileName = "logs";
    public static EnumSet<AuditLevel> auditLevels = EnumSet.allOf(AuditLevel.class);

    public static void entry(String message, Enum<AuditLevel> level) {
        if (auditLevels.contains((AuditLevel) level)) {
            try {
                FileWriter file = new FileWriter(fileName, true);
                String time = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss   ").format(LocalDateTime.now());
                file.write(String.format("%15s%-15s%s%n", time, level.name(), message));
                file.flush();
                file.close();
            }
            catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}