# Panduan Lengkap StudentDashboard.java - Sistem Ujian Online untuk Mahasiswa

## 1. Overview Kelas
StudentDashboard adalah kelas yang menangani antarmuka dan fungsionalitas untuk mahasiswa dalam sistem ujian online.

### 1.1 Atribut Utama
```java
public class StudentDashboard {
    private final Student student;          // Data mahasiswa
    private final Stage stage;             // JavaFX stage
    private final Map<String, Exam> exams; // Daftar ujian
    private final Map<String, Map<String, List<Answer>>> studentAnswers; // Jawaban
    private final Map<String, List<KeyLogEntry>> examLogs; // Log aktivitas
}
```

## 2. Konstruktor dan Inisialisasi

```java
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
```

## 3. Tampilan Dashboard Utama

### 3.1 Method Show
```java
public void show() {
    VBox dashboard = new VBox(10);
    dashboard.setPadding(new Insets(20));
    dashboard.setAlignment(Pos.TOP_CENTER);

    // Welcome Label
    Label welcomeLabel = new Label("Welcome, " + student.getUsername());
    welcomeLabel.setStyle("-fx-font-size: 20px;");

    // Tab System
    TabPane tabPane = new TabPane();
    tabPane.getTabs().addAll(
        createAvailableExamsTab(),
        createGradesTab()
    );

    // Logout Button
    Button logoutBtn = new Button("Logout");
    logoutBtn.setOnAction(e -> new ExamSystemUI().start(stage));

    dashboard.getChildren().addAll(welcomeLabel, tabPane, logoutBtn);
    Scene scene = new Scene(dashboard, 800, 600);
    stage.setScene(scene);
}
```

## 4. Tab Ujian yang Tersedia

### 4.1 Available Exams Tab
```java
private Tab createAvailableExamsTab() {
    Tab tab = new Tab("Available Exams");
    tab.setClosable(false);

    // List Ujian
    ListView<String> examList = new ListView<>();
    for (Exam exam : exams.values()) {
        if (exam.isStudentEligible(student)) {
            examList.getItems().add(exam.getTitle());
        }
    }

    // Tombol Mulai Ujian
    Button takeExamBtn = new Button("Take Exam");
    takeExamBtn.setOnAction(e -> {
        String selectedExam = examList.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            showExamInterface(findExamByTitle(selectedExam));
        }
    });
}
```

## 5. Antarmuka Ujian

### 5.1 Exam Interface
```java
private void showExamInterface(Exam exam) {
    VBox examBox = new VBox(15);
    
    // Inisialisasi Sesi Ujian
    ExamSession session = new ExamSession();
    session.addActiveStudent(student.getUserId());
    session.startSession();
    ExamKeyLogger keyLogger = session.getKeyLogger(student.getUserId());

    // Setup Soal dan Jawaban
    VBox questionsBox = new VBox(10);
    Map<String, ToggleGroup> answerGroups = new HashMap<>();
    List<Answer> answers = new ArrayList<>();

    // Render Soal
    for (Question question : exam.getQuestions()) {
        VBox questionBox = createQuestionBox(question, answerGroups);
        questionsBox.getChildren().add(questionBox);
    }

    // Monitoring Aktivitas
    setupActivityMonitoring(stage, session, keyLogger);
}
```

### 5.2 Question Box Creation
```java
private VBox createQuestionBox(Question question, Map<String, ToggleGroup> answerGroups) {
    VBox questionBox = new VBox(5);
    Label questionLabel = new Label(question.getContent());
    ToggleGroup group = new ToggleGroup();
    answerGroups.put(question.getQuestionId(), group);

    // Setup Pilihan Jawaban
    VBox optionsBox = new VBox(5);
    for (String option : question.getOptions()) {
        RadioButton rb = new RadioButton(option);
        rb.setToggleGroup(group);
        optionsBox.getChildren().add(rb);
    }

    questionBox.getChildren().addAll(questionLabel, optionsBox);
    return questionBox;
}
```

## 6. Sistem Pengumpulan Jawaban

### 6.1 Submit Exam
```java
private void submitExam(Exam exam, Map<String, ToggleGroup> answerGroups,
        List<Answer> answers, ExamSession session) {
    // Collect Answers
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

    // Save Answers and Logs
    saveAnswersAndLogs(exam, answers, session);
}
```

## 7. Sistem Monitoring Aktivitas

### 7.1 Activity Monitoring
```java
private void setupActivityMonitoring(Stage stage, ExamSession session, 
        ExamKeyLogger keyLogger) {
    // Window Focus Monitoring
    stage.focusedProperty().addListener((obs, oldValue, newValue) -> {
        if (!newValue && session.isActive()) {
            keyLogger.logWindowUnfocused();
        }
    });

    // Keyboard Activity Monitoring
    scene.addEventFilter(KeyEvent.ANY, event -> {
        if (session.isActive()) {
            keyLogger.logKeyEvent(event);
        }
    });
}
```

## 8. Tab Nilai

### 8.1 Grades Tab
```java
private Tab createGradesTab() {
    Tab tab = new Tab("My Grades");
    
    // Table View untuk Nilai
    TableView<GradeEntry> gradeTable = new TableView<>();
    
    // Kolom Nama Ujian
    TableColumn<GradeEntry, String> examCol = new TableColumn<>("Exam");
    examCol.setCellValueFactory(data -> data.getValue().examNameProperty());
    
    // Kolom Nilai
    TableColumn<GradeEntry, String> scoreCol = new TableColumn<>("Score");
    scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());
    
    // Load Nilai
    loadGrades(gradeTable);
}
```

## 9. Helper Classes

### 9.1 Grade Entry
```java
private static class GradeEntry {
    private final String examName;
    private final String score;

    public GradeEntry(String examName, String score) {
        this.examName = examName;
        this.score = score;
    }

    public StringProperty examNameProperty() {
        return new SimpleStringProperty(examName);
    }

    public StringProperty scoreProperty() {
        return new SimpleStringProperty(score);
    }
}
```

## 10. Integrasi dengan Komponen Lain

### 10.1 Dengan ExamSystem
- Mengakses daftar ujian yang tersedia
- Validasi eligibilitas ujian
- Pengumpulan jawaban

### 10.2 Dengan Monitoring System
- Logging aktivitas keyboard
- Monitoring focus window
- Deteksi kecurangan

### 10.3 Dengan Grading System
- Menampilkan nilai ujian
- Status penilaian
- Riwayat ujian

## 11. Fitur Keamanan

### 11.1 Anti-Cheating
```java
// Monitoring window focus
stage.focusedProperty().addListener((obs, oldValue, newValue) -> {
    if (!newValue && session.isActive()) {
        keyLogger.logWindowUnfocused();
    }
});

// Monitoring keyboard shortcuts
scene.addEventFilter(KeyEvent.ANY, event -> {
    if (session.isActive()) {
        keyLogger.logKeyEvent(event);
    }
});
```

## 12. Best Practices

1. **Separation of Concerns**
   - UI terpisah dari logika bisnis
   - Modular design
   - Clean code principles

2. **Error Handling**
   - Validasi input
   - Handling session timeout
   - Feedback yang jelas

3. **UI/UX**
   - Layout yang terstruktur
   - Navigasi intuitif
   - Feedback visual

StudentDashboard.java berperan sebagai:
1. Interface ujian mahasiswa
2. Sistem pengumpulan jawaban
3. Monitoring aktivitas
4. Penampilan nilai
5. Manajemen sesi ujian

Dengan struktur ini, StudentDashboard.java menjadi pusat interaksi mahasiswa dengan sistem ujian online, menyediakan antarmuka yang user-friendly sambil tetap menjaga integritas ujian.