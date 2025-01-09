package com.end.lms;

import com.end.lms.ui.ExamSystemUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Exam {
    private final String examId;
    private final String title;
    private boolean isPublished;
    private final List<Question> questions;

    public Exam(String examId, String title) {
        this.examId = examId;
        this.title = title;
        this.isPublished = false;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void publish() {
        if (!questions.isEmpty()) {
            this.isPublished = true;
            System.out.println("Exam " + title + " has been published.");
        } else {
            System.out.println("Cannot publish exam without questions.");
        }
    }

    public float calculateScore(List<Answer> answers) {
        if (answers == null || answers.isEmpty())
            return 0;

        float totalScore = 0;
        for (Answer answer : answers) {
            Question question = findQuestionForAnswer(answer);
            if (question != null && question.validateAnswer(answer.getContent())) {
                totalScore += 20;
            }
        }
        return totalScore;
    }

    private Question findQuestionForAnswer(Answer answer) {
        return questions.stream()
                .filter(q -> q.getQuestionId().equals(answer.getQuestionId()))
                .findFirst()
                .orElse(null);
    }

    public boolean isStudentEligible(Student student) {

        if (!isPublished())
            return false;

        Map<String, List<Answer>> studentExams = ExamSystemUI.getStudentAnswers().get(student.getUserId());
        return studentExams == null || !studentExams.containsKey(examId);
    }

    public String getExamId() {
        return examId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

}
