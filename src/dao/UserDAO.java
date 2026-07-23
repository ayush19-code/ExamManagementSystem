package dao;
import db.DBConnection;
import model.*;
import java.sql.*;

public class UserDAO {
    public User login(String username, String password) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                if (role.equals("admin")) return new Admin(id, username);
                else return new Student(id, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}