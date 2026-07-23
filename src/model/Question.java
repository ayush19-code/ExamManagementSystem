package model;

public class Question {
    private int id;
    private String subject, questionText, optA, optB, optC, optD, correctOpt;

    public Question(int id, String subject, String questionText,
                    String optA, String optB, String optC, String optD, String correctOpt) {
        this.id = id;
        this.subject = subject;
        this.questionText = questionText;
        this.optA = optA; this.optB = optB;
        this.optC = optC; this.optD = optD;
        this.correctOpt = correctOpt;
    }

    public int getId() { return id; }
    public String getSubject() { return subject; }
    public String getQuestionText() { return questionText; }
    public String getOptA() { return optA; }
    public String getOptB() { return optB; }
    public String getOptC() { return optC; }
    public String getOptD() { return optD; }
    public String getCorrectOpt() { return correctOpt; }
}