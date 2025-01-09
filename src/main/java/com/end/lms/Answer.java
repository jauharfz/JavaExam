package com.end.lms;

public class Answer {
    private String examId;
    private String content;
    private String questionId;
    private boolean isGraded;
    private float totalScore;

    public Answer(String studentId, String examId, String questionId, String content) {
        this.questionId = questionId;
        this.content = content;
        this.isGraded = false;
        this.totalScore = 0.0f;
        this.examId = examId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void submit() {
        validate();

    }

    public boolean validate() {
        return content != null && !content.trim().isEmpty();
    }

    public float getScore() {
        return totalScore;
    }

    public String getContent() {
        return content;
    }

    public boolean isGraded() {
        return isGraded;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float score) {
        this.totalScore = score;
        this.isGraded = true;
    }

    public String getExamId() {
        return examId;
    }

}
