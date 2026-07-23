package gui;
import dao.UserDAO;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    // Dark theme colors
    static final Color BG_DARK     = new Color(18, 18, 28);
    static final Color BG_CARD     = new Color(28, 28, 45);
    static final Color PURPLE      = new Color(138, 43, 226);
    static final Color PURPLE_LIGHT= new Color(180, 100, 255);
    static final Color TEXT_WHITE  = new Color(240, 240, 255);
    static final Color TEXT_GRAY   = new Color(160, 160, 190);
    static final Color INPUT_BG    = new Color(38, 38, 58);
    static final Color BORDER_COLOR= new Color(80, 80, 120);

    public LoginFrame() {
        setTitle("Exam Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(BG_DARK);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_DARK);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(PURPLE, 2, true),
            new EmptyBorder(50, 60, 50, 60)
        ));
        card.setPreferredSize(new Dimension(480, 520));

        JLabel icon = new JLabel("📋", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("EXAM PORTAL");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(PURPLE_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to continue");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLbl = new JLabel("USERNAME");
        userLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        userLbl.setForeground(TEXT_GRAY);
        userLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        userField = new JTextField();
        userField.setMaximumSize(new Dimension(360, 45));
        userField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        userField.setBackground(INPUT_BG);
        userField.setForeground(TEXT_WHITE);
        userField.setCaretColor(TEXT_WHITE);
        userField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(8, 15, 8, 15)
        ));

        JLabel passLbl = new JLabel("PASSWORD");
        passLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        passLbl.setForeground(TEXT_GRAY);
        passLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(360, 45));
        passField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        passField.setBackground(INPUT_BG);
        passField.setForeground(TEXT_WHITE);
        passField.setCaretColor(TEXT_WHITE);
        passField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(8, 15, 8, 15)
        ));

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setMaximumSize(new Dimension(360, 50));
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginBtn.setBackground(PURPLE);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin());

        JLabel hint = new JLabel("admin: admin1/admin123  |  student: student1/pass123");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(new Color(100, 100, 140));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icon);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(sub);
        card.add(Box.createVerticalStrut(35));
        card.add(userLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(userField);
        card.add(Box.createVerticalStrut(20));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(passField);
        card.add(Box.createVerticalStrut(30));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(hint);

        root.add(card);
        add(root);
        setVisible(true);
    }

    private void doLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        UserDAO dao = new UserDAO();
        User user = dao.login(username, password);
        if (user != null) {
            dispose();
            if (user instanceof Admin) new AdminDashboard((Admin) user);
            else new StudentDashboard((Student) user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }
}