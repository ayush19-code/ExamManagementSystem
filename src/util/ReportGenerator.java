package util;
import java.io.*;
import java.time.LocalDateTime;

public class ReportGenerator {
    public static void generate(String studentName, String subject, int score, int total) {
        String filename = studentName + "_" + subject + "_report.txt";
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("============================\n");
            fw.write("       EXAM REPORT\n");
            fw.write("============================\n");
            fw.write("Student  : " + studentName + "\n");
            fw.write("Subject  : " + subject + "\n");
            fw.write("Score    : " + score + "/" + total + "\n");
            fw.write("Percent  : " + (score * 100 / total) + "%\n");
            fw.write("Result   : " + (score >= total / 2 ? "PASS ✓" : "FAIL ✗") + "\n");
            fw.write("Date     : " + LocalDateTime.now() + "\n");
            fw.write("============================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}