package com.end.lms.ui;

import com.end.lms.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.util.*;

public class LecturerDashboard {
    private final Lecturer lecturer;
    private final Stage stage;
    private final Map<String, Exam> exams;
    private final Map<String, Map<String, List<Answer>>> studentAnswers;
    private final Map<String, List<KeyLogEntry>> examLogs;

    public LecturerDashboard(Stage stage, Lecturer lecturer,
            Map<String, Exam> exams,
            Map<String, Map<String, List<Answer>>> studentAnswers,
            Map<String, List<KeyLogEntry>> examLogs) {
        this.stage = stage;
        this.lecturer = lecturer;
        this.exams = exams;
        this.studentAnswers = studentAnswers;
        this.examLogs = examLogs;
    }

    public void show() {
        VBox dashboard = new VBox(10);
        dashboard.setPadding(new Insets(20));
        dashboard.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Welcome, " + lecturer.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 20px;");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createExamManagementTab(),
                createActivityLogsTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> new ExamSystemUI().start(stage));

        dashboard.getChildren().addAll(welcomeLabel, tabPane, logoutBtn);
        Scene scene = new Scene(dashboard, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Lecturer Dashboard - " + lecturer.getUsername());
    }

    private Tab createExamManagementTab() {
        Tab tab = new Tab("Exam Management");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<ExamEntry> examTable = new TableView<>();
        examTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ExamEntry, String> titleCol = new TableColumn<>("Exam Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().exam.getTitle()));

        TableColumn<ExamEntry, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().exam.isPublished() ? "Published" : "Draft"));

        examTable.getColumns().addAll(titleCol, statusCol);
        loadExams(examTable);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button viewResultsBtn = new Button("View Results");
        Button editScoresBtn = new Button("Edit Scores");

        viewResultsBtn.setOnAction(e -> {
            ExamEntry selected = examTable.getSelectionModel().getSelectedItem();
            if (selected != null)
                showExamResults(selected.exam);
        });

        editScoresBtn.setOnAction(e -> {
            ExamEntry selected = examTable.getSelectionModel().getSelectedItem();
            if (selected != null)
                showScoreEditor(selected.exam);
        });

        buttonBox.getChildren().addAll(viewResultsBtn, editScoresBtn);
        content.getChildren().addAll(new Label("Exams:"), examTable, buttonBox);
        tab.setContent(content);
        return tab;
    }

    private Tab createActivityLogsTab() {
        Tab tab = new Tab("Activity Logs");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        ComboBox<String> examSelector = new ComboBox<>();
        exams.values().forEach(exam -> examSelector.getItems().add(exam.getTitle()));

        ListView<String> logsList = new ListView<>();
        examSelector.setOnAction(e -> {
            String selectedTitle = examSelector.getValue();
            if (selectedTitle != null) {
                Exam selectedExam = findExamByTitle(selectedTitle);
                loadActivityLogs(selectedExam, logsList);
            }
        });

        content.getChildren().addAll(
                new Label("Select Exam:"),
                examSelector,
                new Label("Activity Logs:"),
                logsList);
        tab.setContent(content);
        return tab;
    }

    private void loadResults(Exam exam, TableView<StudentResult> table) {
        studentAnswers.forEach((studentId, examAnswers) -> {
            List<Answer> answers = examAnswers.get(exam.getExamId());
            if (answers != null && !answers.isEmpty()) {

                float score = answers.get(0).getTotalScore();
                table.getItems().add(new StudentResult(studentId, score));
            }
        });
    }

    private void showExamResults(Exam exam) {
        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(20));
        resultsBox.setAlignment(Pos.TOP_CENTER);

        TableView<StudentResult> resultsTable = new TableView<>();
        resultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StudentResult, String> studentCol = new TableColumn<>("Student ID");
        studentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().studentId));

        TableColumn<StudentResult, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().score)));

        resultsTable.getColumns().addAll(studentCol, scoreCol);
        loadResults(exam, resultsTable);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> show());

        resultsBox.getChildren().addAll(
                new Label("Results: " + exam.getTitle()),
                resultsTable,
                backBtn);

        Scene scene = new Scene(resultsBox, 800, 600);
        stage.setScene(scene);
    }

    private void showScoreEditor(Exam exam) {
        VBox editorBox = new VBox(10);
        editorBox.setPadding(new Insets(20));
        editorBox.setAlignment(Pos.TOP_CENTER);

        TableView<ScoreEntry> scoreTable = new TableView<>();
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ScoreEntry, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().studentId));

        TableColumn<ScoreEntry, String> scoreCol = new TableColumn<>("Total Score");
        scoreCol.setCellFactory(col -> new TableCell<ScoreEntry, String>() {
            private final TextField textField = new TextField();

            {
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused)
                        commitEdit();
                });
                textField.setOnAction(e -> commitEdit());
            }

            private void commitEdit() {
                ScoreEntry entry = getTableView().getItems().get(getIndex());
                try {
                    float newScore = Float.parseFloat(textField.getText());
                    if (newScore >= 0 && newScore <= 100) {

                        List<Answer> answers = studentAnswers.get(entry.studentId).get(exam.getExamId());
                        answers.forEach(answer -> answer.setTotalScore(newScore));
                    }
                } catch (NumberFormatException ex) {
                    textField.setText(String.valueOf(entry.answer.getTotalScore()));
                }
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ScoreEntry entry = getTableView().getItems().get(getIndex());
                    textField.setText(String.format("%.1f", entry.answer.getTotalScore()));
                    setGraphic(textField);
                }
            }
        });

        TableColumn<ScoreEntry, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().answer.isGraded() ? "Graded" : "Ungraded"));

        scoreTable.getColumns().addAll(studentCol, scoreCol, statusCol);

        Map<String, Answer> studentTotalScores = new HashMap<>();
        studentAnswers.forEach((studentId, examAnswers) -> {
            List<Answer> answers = examAnswers.get(exam.getExamId());
            if (answers != null && !answers.isEmpty()) {
                Answer firstAnswer = answers.get(0);
                studentTotalScores.put(studentId, firstAnswer);
                scoreTable.getItems().add(new ScoreEntry(studentId, "Q1", firstAnswer));
            }
        });

        Button autoGradeBtn = new Button("Auto Grade");
        Button saveBtn = new Button("Save Changes");
        Button backBtn = new Button("Back");

        autoGradeBtn.setOnAction(e -> {
            studentAnswers.forEach((studentId, examAnswers) -> {
                List<Answer> answers = examAnswers.get(exam.getExamId());
                if (answers != null) {
                    float totalScore = exam.calculateScore(answers);

                    answers.forEach(answer -> answer.setTotalScore(totalScore));
                }
            });
            scoreTable.refresh();
        });

        saveBtn.setOnAction(e -> show());
        backBtn.setOnAction(e -> show());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(autoGradeBtn, saveBtn, backBtn);

        editorBox.getChildren().addAll(
                new Label("Edit Scores: " + exam.getTitle()),
                scoreTable,
                buttonBox);

        Scene scene = new Scene(new ScrollPane(editorBox), 800, 600);
        stage.setScene(scene);
    }

    private void loadExams(TableView<ExamEntry> table) {
        exams.values().forEach(exam -> table.getItems().add(new ExamEntry(exam)));
    }

    private void loadActivityLogs(Exam exam, ListView<String> logsList) {
        logsList.getItems().clear();
        examLogs.forEach((key, logs) -> {
            if (key.contains(exam.getExamId())) {
                String[] parts = key.split("_");
                String studentId = parts[0];
                logsList.getItems().add("Student: " + studentId);
                logs.forEach(log -> logsList.getItems().add("  " + log));
                logsList.getItems().add("");
            }
        });
    }

    private Exam findExamByTitle(String title) {
        return exams.values().stream()
                .filter(e -> e.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    private static class ExamEntry {
        final Exam exam;

        ExamEntry(Exam exam) {
            this.exam = exam;
        }
    }

    private static class StudentResult {
        final String studentId;
        final float score;

        StudentResult(String studentId, float score) {
            this.studentId = studentId;
            this.score = score;
        }
    }

    private static class ScoreEntry {
        final String studentId;
        final String questionId;
        final Answer answer;

        ScoreEntry(String studentId, String questionId, Answer answer) {
            this.studentId = studentId;
            this.questionId = questionId;
            this.answer = answer;
        }
    }
}
