package gui;
import model.Student;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    public StudentDashboard(Student student) {
        setTitle("Student Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(LoginFrame.BG_DARK);

        // Top navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(LoginFrame.BG_CARD);
        navbar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, LoginFrame.PURPLE),
            new EmptyBorder(15, 30, 15, 30)
        ));
        JLabel logo = new JLabel("📋  EXAM PORTAL");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(LoginFrame.PURPLE_LIGHT);
        JLabel userInfo = new JLabel("👤  " + student.getUsername().toUpperCase());
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

        JLabel welcome = new JLabel("Welcome Back, " + student.getUsername() + "!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 30));
        welcome.setForeground(LoginFrame.TEXT_WHITE);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("What would you like to do today?");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(LoginFrame.TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(welcome);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(sub);
        cardPanel.add(Box.createVerticalStrut(50));

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        menuPanel.setBackground(LoginFrame.BG_DARK);

        menuPanel.add(makeCard("📝", "Attempt Exam",
            "Take a new exam", new Color(138, 43, 226),
            e -> new ExamFrame(student)));
        menuPanel.add(makeCard("📊", "My Results",
            "View your scores", new Color(0, 180, 150),
            e -> new ResultFrame(student)));
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
            new EmptyBorder(30, 40, 30, 40)
        ));
        card.setPreferredSize(new Dimension(220, 220));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(emoji, SwingConstants.CENTER);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 40));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("SansSerif", Font.BOLD, 16));
        ttl.setForeground(LoginFrame.TEXT_WHITE);
        ttl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(LoginFrame.TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton("Open");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(140, 38));
        btn.addActionListener(action);

        card.add(ico);
        card.add(Box.createVerticalStrut(12));
        card.add(ttl);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(18));
        card.add(btn);
        return card;
    }
}