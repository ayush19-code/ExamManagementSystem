package gui;
import dao.ExamDAO;
import db.DBConnection;
import model.Admin;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private ExamDAO dao = new ExamDAO();

    public AdminDashboard(Admin admin) {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(LoginFrame.BG_DARK);

        // Navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(LoginFrame.BG_CARD);
        navbar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, LoginFrame.PURPLE),
            new EmptyBorder(15, 30, 15, 30)
        ));
        JLabel logo = new JLabel("📋  EXAM PORTAL — ADMIN");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(LoginFrame.PURPLE_LIGHT);
        JLabel userInfo = new JLabel("🔑  " + admin.getUsername().toUpperCase());
        userInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        userInfo.setForeground(LoginFrame.TEXT_GRAY);
        navbar.add(logo, BorderLayout.WEST);
        navbar.add(userInfo, BorderLayout.EAST);
        root.add(navbar, BorderLayout.NORTH);

        // Center
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(LoginFrame.BG_DARK);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(LoginFrame.BG_DARK);

        JLabel title = new JLabel("Admin Control Panel");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(LoginFrame.TEXT_WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Manage exams, questions and results");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(LoginFrame.TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(title);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(sub);
        cardPanel.add(Box.createVerticalStrut(50));

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        menuPanel.setBackground(LoginFrame.BG_DARK);

        menuPanel.add(makeCard("➕", "Add Question",
            "Add new MCQ", new Color(138, 43, 226),
            e -> showAddQuestion()));
        menuPanel.add(makeCard("📊", "All Results",
            "View student scores", new Color(0, 180, 150),
            e -> showAllResults()));
        menuPanel.add(makeCard("📋", "All Questions",
            "View question bank", new Color(230, 150, 0),
            e -> showAllQuestions()));
        menuPanel.add(makeCard("🚪", "Logout",
            "Sign out safely", new Color(200, 50, 80),
            e -> { dispose(); SwingUtilities.invokeLater(() -> new LoginFrame()); }));

        cardPanel.add(menuPanel);
        center.add(cardPanel);
        root.add(center, BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }

    private JPanel makeCard(String emoji, String title, String subtitle,
                             Color color, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(LoginFrame.BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(color, 2, true),
            new EmptyBorder(25, 35, 25, 35)
        ));
        card.setPreferredSize(new Dimension(200, 210));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(emoji, SwingConstants.CENTER);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 36));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("SansSerif", Font.BOLD, 15));
        ttl.setForeground(LoginFrame.TEXT_WHITE);
        ttl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(LoginFrame.TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton("Open");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(130, 35));
        btn.addActionListener(action);

        card.add(ico);
        card.add(Box.createVerticalStrut(10));
        card.add(ttl);
        card.add(Box.createVerticalStrut(5));
        card.add(sub);
        card.add(Box.createVerticalStrut(15));
        card.add(btn);
        return card;
    }

    private void showAddQuestion() {
        JTextField subjectF  = new JTextField();
        JTextField questionF = new JTextField();
        JTextField optAF = new JTextField();
        JTextField optBF = new JTextField();
        JTextField optCF = new JTextField();
        JTextField optDF = new JTextField();
        JComboBox<String> correctBox = new JComboBox<>(new String[]{"A","B","C","D"});

        Object[] fields = {
            "Subject:",     subjectF,
            "Question:",    questionF,
            "Option A:",    optAF,
            "Option B:",    optBF,
            "Option C:",    optCF,
            "Option D:",    optDF,
            "Correct Ans:", correctBox
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
            "Add New Question", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            dao.addQuestion(subjectF.getText(), questionF.getText(),
                optAF.getText(), optBF.getText(),
                optCF.getText(), optDF.getText(),
                (String) correctBox.getSelectedItem());
            JOptionPane.showMessageDialog(this, "✅ Question added successfully!");
        }
    }

    private void showAllResults() {
        JFrame f = new JFrame("All Student Results");
        f.setSize(800, 500);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(LoginFrame.BG_DARK);

        String[] cols = {"Student","Subject","Score","Total","%","Result","Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT u.username, r.subject, r.score, r.total, r.exam_date " +
                         "FROM results r JOIN users u ON r.student_id=u.id ORDER BY r.exam_date DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                int score = rs.getInt("score");
                int total = rs.getInt("total");
                model.addRow(new Object[]{
                    rs.getString("username"), rs.getString("subject"),
                    score, total, (score*100/total)+"%",
                    score>=total/2 ? "PASS":"FAIL",
                    rs.getTimestamp("exam_date").toString().substring(0,16)
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }

        f.add(new JScrollPane(styleTable(new JTable(model))));
        f.setVisible(true);
    }

    private void showAllQuestions() {
        JFrame f = new JFrame("Question Bank");
        f.setSize(900, 500);
        f.setLocationRelativeTo(null);

        String[] cols = {"ID","Subject","Question","A","B","C","D","Answer"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT * FROM questions ORDER BY subject");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("subject"),
                    rs.getString("question_text"),
                    rs.getString("option_a"), rs.getString("option_b"),
                    rs.getString("option_c"), rs.getString("option_d"),
                    rs.getString("correct_option")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }

        f.add(new JScrollPane(styleTable(new JTable(model))));
        f.setVisible(true);
    }

    private JTable styleTable(JTable table) {
        table.setBackground(LoginFrame.BG_CARD);
        table.setForeground(LoginFrame.TEXT_WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setGridColor(LoginFrame.BORDER_COLOR);
        table.getTableHeader().setBackground(LoginFrame.PURPLE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        return table;
    }
}