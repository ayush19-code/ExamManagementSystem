package model;

public class Student extends User {
    public Student(int id, String username) {
        super(id, username, "student");
    }

    @Override
    public void showDashboard() {
        System.out.println("Student: " + username);
    }
}