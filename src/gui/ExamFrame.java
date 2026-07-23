package gui;
import dao.ExamDAO;
import model.*;
import util.ReportGenerator;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ExamFrame extends JFrame {
    private List<Question> questions;
    private Map<Integer, String> answers = new HashMap<>();
    private int currentIndex = 0;
    private Student student;
    private String subject;

    private JLabel progressLabel, questionLabel;
    private JRadioButton optA, optB, optC, optD;
    private ButtonGroup group;
    private JButton prevBtn, nextBtn, submitBtn;

    // --- Timer fields ---
    private static final int SECONDS_PER_QUESTION = 60; // 1 minute per question
    private int timeLeftSeconds;
    private javax.swing.Timer countdownTimer;
    private JLabel timerLabel;

    public ExamFrame(Student student) {
        ExamDAO dao = new ExamDAO();
        List<String> subjects = dao.getSubjects();

        if (subjects.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No exams available in database!");
            return;
        }

        subject = (String) JOptionPane.showInputDialog(null,
            "Select Subject:", "Choose Exam",
            JOptionPane.QUESTION_MESSAGE, null,
            subjects.toArray(), subjects.get(0));

        if (subject == null) return;

        this.student = student;
        questions = dao.getQuestions(subject);

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No questions for: " + subject);
            return;
        }

        // Total time = 1 minute per question
        timeLeftSeconds = questions.size() * SECONDS_PER_QUESTION;

        buildUI();
        loadQuestion();
        startTimer();
        setVisible(true);
    }

    // ---------------------------------------------------------------
    // Timer logic
    // ---------------------------------------------------------------

    private void startTimer() {
        countdownTimer = new javax.swing.Timer(1000, e -> {
            timeLeftSeconds--;
            updateTimerLabel();
            if (timeLeftSeconds <= 0) {
                countdownTimer.stop();
                autoSubmit();
            }
        });
        countdownTimer.start();
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        int mins = timeLeftSeconds / 60;
        int secs = timeLeftSeconds % 60;
        timerLabel.setText(String.format("⏱  %02d:%02d", mins, secs));

        // Color coding: green → orange (≤2 min) → red (≤30 sec)
        if (timeLeftSeconds <= 30) {
            timerLabel.setForeground(new Color(192, 57, 43));   // red
        } else if (timeLeftSeconds <= 120) {
            timerLabel.setForeground(new Color(230, 126, 34));  // orange
        } else {
            timerLabel.setForeground(new Color(39, 174, 96));   // green
        }
    }

    private void autoSubmit() {
        saveAnswer();
        JOptionPane.showMessageDialog(this,
            "⏰ Time's up! Your exam is being submitted automatically.",
            "Time Over", JOptionPane.WARNING_MESSAGE);
        finishExam();
    }

    // ---------------------------------------------------------------
    // UI building
    // ---------------------------------------------------------------

    private void buildUI() {
        setTitle("Exam: " + subject);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout(10, 10));

        // --- NORTH: subject label + progress + timer ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        JLabel subjectLbl = new JLabel("Subject: " + subject);
        subjectLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        subjectLbl.setForeground(new Color(30, 60, 114));
        topPanel.add(subjectLbl, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightTop.setBackground(Color.WHITE);

        progressLabel = new JLabel("", SwingConstants.RIGHT);
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressLabel.setForeground(new Color(30, 60, 114));

        timerLabel = new JLabel("", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        timerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        timerLabel.setOpaque(true);
        timerLabel.setBackground(new Color(245, 248, 255));

        rightTop.add(progressLabel);
        rightTop.add(timerLabel);
        topPanel.add(rightTop, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER: question + options ---
        questionLabel = new JLabel("", SwingConstants.LEFT);
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        questionLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        questionLabel.setBackground(new Color(245, 248, 255));
        questionLabel.setOpaque(true);

        JPanel optPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optPanel.setBackground(Color.WHITE);
        optPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        group = new ButtonGroup();
        optA = makeOption(); optB = makeOption();
        optC = makeOption(); optD = makeOption();
        for (JRadioButton rb : new JRadioButton[]{optA, optB, optC, optD}) {
            group.add(rb);
            optPanel.add(rb);
        }

        JPanel midPanel = new JPanel(new BorderLayout(0, 10));
        midPanel.setBackground(Color.WHITE);
        midPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        midPanel.add(questionLabel, BorderLayout.NORTH);
        midPanel.add(optPanel, BorderLayout.CENTER);
        add(midPanel, BorderLayout.CENTER);

        // --- SOUTH: nav buttons ---
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        prevBtn   = makeNavBtn("← Prev",   new Color(149, 165, 166));
        nextBtn   = makeNavBtn("Next →",   new Color(52, 152, 219));
        submitBtn = makeNavBtn("✔ Submit", new Color(39, 174, 96));

        prevBtn.addActionListener(e -> movePrev());
        nextBtn.addActionListener(e -> moveNext());
        submitBtn.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            submitExam();
        });

        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        navPanel.add(submitBtn);
        add(navPanel, BorderLayout.SOUTH);
    }

    private JRadioButton makeOption() {
        JRadioButton rb = new JRadioButton();
        rb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rb.setBackground(Color.WHITE);
        return rb;
    }

    private JButton makeNavBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ---------------------------------------------------------------
    // Question navigation
    // ---------------------------------------------------------------

    private void loadQuestion() {
        Question q = questions.get(currentIndex);
        progressLabel.setText("Question " + (currentIndex + 1) + " / " + questions.size());
        questionLabel.setText("<html><body style='width:500px'><b>Q" +
            (currentIndex + 1) + ".</b> " + q.getQuestionText() + "</body></html>");
        optA.setText("A.  " + q.getOptA());
        optB.setText("B.  " + q.getOptB());
        optC.setText("C.  " + q.getOptC());
        optD.setText("D.  " + q.getOptD());
        group.clearSelection();

        String saved = answers.get(q.getId());
        if ("A".equals(saved))      optA.setSelected(true);
        else if ("B".equals(saved)) optB.setSelected(true);
        else if ("C".equals(saved)) optC.setSelected(true);
        else if ("D".equals(saved)) optD.setSelected(true);

        prevBtn.setEnabled(currentIndex > 0);
        nextBtn.setEnabled(currentIndex < questions.size() - 1);
    }

    private void saveAnswer() {
        int qId = questions.get(currentIndex).getId();
        if (optA.isSelected())      answers.put(qId, "A");
        else if (optB.isSelected()) answers.put(qId, "B");
        else if (optC.isSelected()) answers.put(qId, "C");
        else if (optD.isSelected()) answers.put(qId, "D");
    }

    private void movePrev() {
        saveAnswer();
        currentIndex--;
        loadQuestion();
    }

    private void moveNext() {
        saveAnswer();
        currentIndex++;
        loadQuestion();
    }

    // ---------------------------------------------------------------
    // Submission
    // ---------------------------------------------------------------

    private void submitExam() {
        saveAnswer();
        int unanswered = (int) questions.stream()
            .filter(q -> !answers.containsKey(q.getId())).count();
        if (unanswered > 0) {
            int choice = JOptionPane.showConfirmDialog(this,
                unanswered + " question(s) unanswered. Submit anyway?",
                "Confirm Submit", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                // Resume timer if user cancels
                if (countdownTimer != null && !countdownTimer.isRunning())
                    countdownTimer.start();
                return;
            }
        }
        finishExam();
    }

    private void finishExam() {
        if (countdownTimer != null) countdownTimer.stop();

        int score = 0;
        for (Question q : questions) {
            String given = answers.get(q.getId());
            if (q.getCorrectOpt().equals(given)) score++;
        }

        new ExamDAO().saveResult(student.getId(), subject, score, questions.size());
        ReportGenerator.generate(student.getUsername(), subject, score, questions.size());

        JOptionPane.showMessageDialog(this,
            "✅ Exam Submitted!\n\nScore: " + score + " / " + questions.size() +
            "\nPercentage: " + (score * 100 / questions.size()) + "%" +
            "\nResult: " + (score >= questions.size() / 2 ? "PASS" : "FAIL") +
            "\n\nReport saved as text file.",
            "Result", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}