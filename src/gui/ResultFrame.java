package gui;
import db.DBConnection;
import model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;

public class ResultFrame extends JFrame {
    public ResultFrame(Student student) {
        setTitle("My Results - " + student.getUsername());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {"#", "Subject", "Score", "Total", "Percentage", "Result", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT subject, score, total, exam_date FROM results " +
                "WHERE student_id=? ORDER BY exam_date DESC");
            ps.setInt(1, student.getId());
            ResultSet rs = ps.executeQuery();
            int row = 1;
            while (rs.next()) {
                int score = rs.getInt("score");
                int total = rs.getInt("total");
                int pct   = score * 100 / total;
                model.addRow(new Object[]{
                    row++,
                    rs.getString("subject"),
                    score, total,
                    pct + "%",
                    pct >= 50 ? "PASS" : "FAIL",
                    rs.getTimestamp("exam_date").toString().substring(0, 16)
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }

        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);

        // Color PASS/FAIL column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                if ("PASS".equals(v)) {
                    setForeground(new Color(39, 174, 96));
                } else {
                    setForeground(new Color(192, 57, 43));
                }
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        JLabel title = new JLabel("Results for: " + student.getUsername().toUpperCase());
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setForeground(new Color(30, 60, 114));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel);
        setVisible(true);
    }
}