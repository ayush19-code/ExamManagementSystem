package dao;
import db.DBConnection;
import model.Question;
import java.sql.*;
import java.util.*;

public class ExamDAO {

    public List<String> getSubjects() {
        List<String> subjects = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT DISTINCT subject FROM questions");
            while (rs.next()) subjects.add(rs.getString("subject"));
        } catch (SQLException e) { e.printStackTrace(); }
        return subjects;
    }

    public List<Question> getQuestions(String subject) {
        List<Question> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM questions WHERE subject=?");
            ps.setString(1, subject);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Question(
                    rs.getInt("id"),
                    rs.getString("subject"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_option")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void saveResult(int studentId, String subject, int score, int total) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO results(student_id, subject, score, total) VALUES(?,?,?,?)");
            ps.setInt(1, studentId);
            ps.setString(2, subject);
            ps.setInt(3, score);
            ps.setInt(4, total);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void addQuestion(String subject, String qText,
                            String a, String b, String c, String d, String correct) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO questions(subject,question_text,option_a,option_b," +
                "option_c,option_d,correct_option) VALUES(?,?,?,?,?,?,?)");
            ps.setString(1, subject); ps.setString(2, qText);
            ps.setString(3, a);      ps.setString(4, b);
            ps.setString(5, c);      ps.setString(6, d);
            ps.setString(7, correct);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}