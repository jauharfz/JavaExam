package com.end.lms.ui;

import com.end.lms.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class StudentDashboard {
    private final Student student;
    private final Stage stage;
    private final Map<String, Exam> exams;
    private final Map<String, Map<String, List<Answer>>> studentAnswers;
    private final Map<String, List<KeyLogEntry>> examLogs;

    public StudentDashboard(Stage stage, Student student,
            Map<String, Exam> exams,
            Map<String, Map<String, List<Answer>>> studentAnswers,
            Map<String, List<KeyLogEntry>> examLogs) {
        this.stage = stage;
        this.student = student;
        this.exams = exams;
        this.studentAnswers = studentAnswers;
        this.examLogs = examLogs;
    }

    public void show() {
        VBox dashboard = new VBox(10);
        dashboard.setPadding(new Insets(20));
        dashboard.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Welcome, " + student.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 20px;");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createAvailableExamsTab(),
                createGradesTab());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> new ExamSystemUI().start(stage));

        dashboard.getChildren().addAll(welcomeLabel, tabPane, logoutBtn);
        Scene scene = new Scene(dashboard, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Student Dashboard - " + student.getUsername());
    }

    private Tab createAvailableExamsTab() {
        Tab tab = new Tab("Available Exams");
        tab.setClosable(false);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        ListView<String> examList = new ListView<>();
        for (Exam exam : exams.values()) {
            if (exam.isStudentEligible(student)) {
                examList.getItems().add(exam.getTitle());
            }
        }

        Button takeExamBtn = new Button("Take Exam");
        takeExamBtn.setOnAction(e -> {
            String selectedExam = examList.getSelectionModel().getSelectedItem();
            if (selectedExam != null) {
                showExamInterface(findExamByTitle(selectedExam));
            }
        });

        content.getChildren().addAll(new Label("Select an exam:"), examList, takeExamBtn);
        tab.setContent(content);
        return tab;
    }

    private Tab createGradesTab() {
        Tab tab = new Tab("My Grades");
        tab.setClosable(false);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<GradeEntry> gradeTable = new TableView<>();
        TableColumn<GradeEntry, String> examCol = new TableColumn<>("Exam");
        examCol.setCellValueFactory(data -> data.getValue().examNameProperty());
        TableColumn<GradeEntry, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        gradeTable.getColumns().addAll(examCol, scoreCol);
        loadGrades(gradeTable);

        content.getChildren().addAll(new Label("Your Grades:"), gradeTable);
        tab.setContent(content);
        return tab;
    }

    private void showExamInterface(Exam exam) {
        VBox examBox = new VBox(15);
        examBox.setPadding(new Insets(20));
        examBox.setAlignment(Pos.TOP_CENTER);

        Label examTitle = new Label(exam.getTitle());
        examTitle.setStyle("-fx-font-size: 18px;");

        ExamSession session = new ExamSession();
        session.addActiveStudent(student.getUserId());
        session.startSession();
        ExamKeyLogger keyLogger = session.getKeyLogger(student.getUserId());

        VBox questionsBox = new VBox(10);
        Map<String, ToggleGroup> answerGroups = new HashMap<>();
        List<Answer> answers = new ArrayList<>();

        for (Question question : exam.getQuestions()) {
            VBox questionBox = new VBox(5);
            Label questionLabel = new Label(question.getContent());
            ToggleGroup group = new ToggleGroup();
            answerGroups.put(question.getQuestionId(), group);

            VBox optionsBox = new VBox(5);
            for (String option : question.getOptions()) {
                RadioButton rb = new RadioButton(option);
                rb.setToggleGroup(group);
                optionsBox.getChildren().add(rb);
            }

            questionBox.getChildren().addAll(questionLabel, optionsBox);
            questionsBox.getChildren().add(questionBox);
        }

        Button submitBtn = new Button("Submit Exam");
        submitBtn.setOnAction(e -> {
            session.endSession();
            submitExam(exam, answerGroups, answers, session);
        });

        ScrollPane scrollPane = new ScrollPane(questionsBox);
        scrollPane.setFitToWidth(true);

        examBox.getChildren().addAll(examTitle, scrollPane, submitBtn);

        stage.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue && session.isActive()) {
                keyLogger.logWindowUnfocused();
            }
        });

        Scene examScene = new Scene(examBox, 800, 600);
        examScene.addEventFilter(KeyEvent.ANY, event -> {
            if (session.isActive()) {
                keyLogger.logKeyEvent(event);
            }
        });

        stage.setScene(examScene);
        stage.setTitle("Exam: " + exam.getTitle());
    }

    private void submitExam(Exam exam, Map<String, ToggleGroup> answerGroups,
            List<Answer> answers, ExamSession session) {
        for (Question question : exam.getQuestions()) {
            ToggleGroup group = answerGroups.get(question.getQuestionId());
            RadioButton selectedRB = (RadioButton) group.getSelectedToggle();
            if (selectedRB != null) {
                String answer = selectedRB.getText().substring(0, 1);
                Answer studentAnswer = new Answer(student.getUserId(),
                        exam.getExamId(), question.getQuestionId(), answer);
                answers.add(studentAnswer);
                student.submitAnswer(studentAnswer);
            }
        }

        studentAnswers.computeIfAbsent(student.getUserId(), k -> new HashMap<>())
                .put(exam.getExamId(), answers);

        String logKey = student.getUserId() + "_" + exam.getExamId();
        examLogs.put(logKey, session.getStudentCheatingLogs(student.getUserId()));

        session.endSession();
        show();
    }

    private void loadGrades(TableView<GradeEntry> gradeTable) {
        Map<String, List<Answer>> studentExamAnswers = studentAnswers.getOrDefault(student.getUserId(),
                new HashMap<>());

        studentExamAnswers.forEach((examId, answers) -> {
            Exam exam = exams.get(examId);
            if (exam != null && !answers.isEmpty()) {
                String scoreText;
                if (answers.get(0).isGraded()) {
                    scoreText = String.format("%.1f", answers.get(0).getScore());
                } else {
                    scoreText = "Ungraded";
                }
                gradeTable.getItems().add(new GradeEntry(exam.getTitle(), scoreText));
            }
        });
    }

    private Exam findExamByTitle(String title) {
        return exams.values().stream()
                .filter(e -> e.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    private static class GradeEntry {
        private final String examName;
        private final String score;

        public GradeEntry(String examName, String score) {
            this.examName = examName;
            this.score = score;
        }

        public javafx.beans.property.StringProperty examNameProperty() {
            return new javafx.beans.property.SimpleStringProperty(examName);
        }

        public javafx.beans.property.StringProperty scoreProperty() {
            return new javafx.beans.property.SimpleStringProperty(score);
        }
    }
}
