package com.end.lms.ui;

import com.end.lms.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class ExamSystemUI extends Application {
    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, Exam> exams = new HashMap<>();
    private static final Map<String, Map<String, List<Answer>>> studentAnswers = new HashMap<>();
    private static final Map<String, List<KeyLogEntry>> examLogs = new HashMap<>();
    private static final String ICON_PATH = "/com/end/lms/icon.png";

    @Override
    public void start(Stage primaryStage) {
        initializeData();
        setStageIcon(primaryStage);
        showLoginScreen(primaryStage);
    }

    public static Map<String, Map<String, List<Answer>>> getStudentAnswers() {
        return studentAnswers;
    }

    private void setStageIcon(Stage stage) {
        try {

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ICON_PATH)));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Warning: Could not load application icon");
        }
    }

    private void showLoginScreen(Stage stage) {
        VBox loginBox = createLoginBox(stage);
        Scene scene = new Scene(loginBox, 400, 300);
        stage.setTitle("Exam System Login");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLoginBox(Stage stage) {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("Exam System Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setMaxWidth(250);

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> handleLogin(stage, usernameField.getText(),
                passwordField.getText(), statusLabel));

        loginBox.getChildren().addAll(
                titleLabel,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                loginButton,
                statusLabel);

        return loginBox;
    }

    private void handleLogin(Stage stage, String username, String password, Label statusLabel) {
        User user = users.get(username);
        if (user != null && user.validatePassword(password)) {
            if (user instanceof Student) {
                showStudentDashboard(stage, (Student) user);
            } else {
                showLecturerDashboard(stage, (Lecturer) user);
            }
        } else {
            statusLabel.setText("Invalid username or password!");
        }
    }

    private void initializeData() {

        users.put("student", new Student("S001", "student", "pass123"));
        users.put("student2", new Student("S002", "student2", "pass123"));
        users.put("student3", new Student("S003", "student3", "pass123"));
        users.put("lecturer", new Lecturer("L001", "lecturer", "pass123"));

        Exam exam = new Exam("E001", "Programming Basics");

        addSampleQuestions(exam);

        exam.publish();
        exams.put(exam.getExamId(), exam);

        Exam exam2 = new Exam("E002", "Programming Basics2");

        addSampleQuestions2(exam2);

        exam2.publish();
        exams.put(exam2.getExamId(), exam2);
    }

    private void addSampleQuestions2(Exam exam) {

        String[][] questions = {
                { "What is Java?", "A. Coffee", "B. Programming Language", "C. Island", "B" },
                { "Which is not a primitive type?", "A. int", "B. String", "C. boolean", "B" },
                { "What is the main purpose of JVM?", "A. To compile Java code", "B. To provide platform independence",
                        "C. To debug Java programs", "B" },
                { "What is the default value of int?", "A. 0", "B. null", "C. undefined", "A" },
                { "Java is:", "A. Compiled Language", "B. Interpreted Language", "C. Both A and B", "C" }
        };

        for (int i = 0; i < questions.length; i++) {
            Question q = new Question("Q" + (i + 1), questions[i][0], 20, "MULTIPLE_CHOICE");
            for (int j = 1; j <= 3; j++) {
                q.addOption(questions[i][j]);
            }
            q.setCorrectAnswer(questions[i][4]);
            exam.addQuestion(q);
        }
    }

    private void addSampleQuestions(Exam exam) {

        String[][] questions = {
                { "What is Java?", "A. Coffee", "B. Programming Language", "C. Island", "B" },
                { "Which is not a primitive type?", "A. int", "B. String", "C. boolean", "B" },
                { "What is the main purpose of JVM?", "A. To compile Java code", "B. To provide platform independence",
                        "C. To debug Java programs", "B" },
                { "What is the default value of int?", "A. 0", "B. null", "C. undefined", "A" },
                { "Java is:", "A. Compiled Language", "B. Interpreted Language", "C. Both A and B", "C" }
        };

        for (int i = 0; i < questions.length; i++) {
            Question q = new Question("Q" + (i + 1), questions[i][0], 20, "MULTIPLE_CHOICE");
            for (int j = 1; j <= 3; j++) {
                q.addOption(questions[i][j]);
            }
            q.setCorrectAnswer(questions[i][4]);
            exam.addQuestion(q);
        }
    }

    private void showStudentDashboard(Stage stage, Student student) {
        StudentDashboard dashboard = new StudentDashboard(stage, student, exams, studentAnswers, examLogs);
        setStageIcon(stage);
        dashboard.show();
    }

    private void showLecturerDashboard(Stage stage, Lecturer lecturer) {
        LecturerDashboard dashboard = new LecturerDashboard(stage, lecturer, exams, studentAnswers, examLogs);
        setStageIcon(stage);
        dashboard.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
