package com.end.lms;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String questionId;
    private String content;
    private String type;
    private List<String> options;
    private String correctAnswer;

    public Question(String questionId, String content, int score, String type) {
        this.questionId = questionId;
        this.content = content;
        this.type = type;
        this.options = new ArrayList<>();
    }

    public void addOption(String option) {
        options.add(option);
    }

    public boolean validateAnswer(String answer) {
        if (type.equals("MULTIPLE_CHOICE")) {
            return answer != null && answer.equals(correctAnswer);
        }

        return answer != null && !answer.trim().isEmpty();
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public List<String> getOptions() {
        return new ArrayList<>(options);
    }
}
