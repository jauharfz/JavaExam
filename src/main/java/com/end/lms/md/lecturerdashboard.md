# Panduan Lengkap LecturerDashboard.java - Sistem Manajemen Ujian

## 1. Overview Kelas
LecturerDashboard adalah kelas yang menangani antarmuka dan fungsionalitas untuk dosen dalam sistem ujian online.

### 1.1 Atribut Utama
```java
public class LecturerDashboard {
    private final Lecturer lecturer;          // Data dosen
    private final Stage stage;               // JavaFX stage
    private final Map<String, Exam> exams;   // Daftar ujian
    private final Map<String, Map<String, List<Answer>>> studentAnswers;  // Jawaban mahasiswa
    private final Map<String, List<KeyLogEntry>> examLogs;  // Log aktivitas
}
```

## 2. Konstruktor dan Inisialisasi

```java
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
```

## 3. Tampilan Dashboard Utama

### 3.1 Method Show
```java
public void show() {
    VBox dashboard = new VBox(10);
    dashboard.setPadding(new Insets(20));
    dashboard.setAlignment(Pos.TOP_CENTER);

    // Welcome Label
    Label welcomeLabel = new Label("Welcome, " + lecturer.getUsername());
    welcomeLabel.setStyle("-fx-font-size: 20px;");

    // Tab System
    TabPane tabPane = new TabPane();
    tabPane.getTabs().addAll(
        createExamManagementTab(),
        createActivityLogsTab()
    );

    // Logout Button
    Button logoutBtn = new Button("Logout");
    logoutBtn.setOnAction(e -> new ExamSystemUI().start(stage));

    dashboard.getChildren().addAll(welcomeLabel, tabPane, logoutBtn);
    Scene scene = new Scene(dashboard, 800, 600);
    stage.setScene(scene);
}
```

## 4. Tab Manajemen Ujian

### 4.1 Exam Management Tab
```java
private Tab createExamManagementTab() {
    Tab tab = new Tab("Exam Management");
    
    // Tabel Ujian
    TableView<ExamEntry> examTable = new TableView<>();
    
    // Kolom Judul Ujian
    TableColumn<ExamEntry, String> titleCol = new TableColumn<>("Exam Title");
    titleCol.setCellValueFactory(data -> 
        new SimpleStringProperty(data.getValue().exam.getTitle()));
    
    // Kolom Status
    TableColumn<ExamEntry, String> statusCol = new TableColumn<>("Status");
    statusCol.setCellValueFactory(data -> 
        new SimpleStringProperty(data.getValue().exam.isPublished() ? 
            "Published" : "Draft"));
    
    // Tombol Aksi
    Button viewResultsBtn = new Button("View Results");
    Button editScoresBtn = new Button("Edit Scores");
}
```

## 5. Manajemen Nilai

### 5.1 Score Editor
```java
private void showScoreEditor(Exam exam) {
    VBox editorBox = new VBox(10);
    
    // Tabel Nilai
    TableView<ScoreEntry> scoreTable = new TableView<>();
    
    // Kolom untuk editing nilai
    TableColumn<ScoreEntry, String> scoreCol = new TableColumn<>("Total Score");
    scoreCol.setCellFactory(col -> new TableCell<ScoreEntry, String>() {
        private final TextField textField = new TextField();
        
        // Logika editing nilai
        private void commitEdit() {
            ScoreEntry entry = getTableView().getItems().get(getIndex());
            try {
                float newScore = Float.parseFloat(textField.getText());
                if (newScore >= 0 && newScore <= 100) {
                    List<Answer> answers = studentAnswers.get(entry.studentId)
                        .get(exam.getExamId());
                    answers.forEach(answer -> answer.setTotalScore(newScore));
                }
            } catch (NumberFormatException ex) {
                textField.setText(String.valueOf(entry.answer.getTotalScore()));
            }
        }
    });
}
```

## 6. Monitoring Aktivitas

### 6.1 Activity Logs Tab
```java
private Tab createActivityLogsTab() {
    Tab tab = new Tab("Activity Logs");
    
    // Selector Ujian
    ComboBox<String> examSelector = new ComboBox<>();
    exams.values().forEach(exam -> 
        examSelector.getItems().add(exam.getTitle()));
    
    // List View untuk log
    ListView<String> logsList = new ListView<>();
    
    // Event handler untuk pemilihan ujian
    examSelector.setOnAction(e -> {
        String selectedTitle = examSelector.getValue();
        if (selectedTitle != null) {
            Exam selectedExam = findExamByTitle(selectedTitle);
            loadActivityLogs(selectedExam, logsList);
        }
    });
}
```

## 7. Pengelolaan Hasil Ujian

### 7.1 View Results
```java
private void showExamResults(Exam exam) {
    // Setup tampilan hasil
    TableView<StudentResult> resultsTable = new TableView<>();
    
    // Kolom ID Mahasiswa
    TableColumn<StudentResult, String> studentCol = 
        new TableColumn<>("Student ID");
    
    // Kolom Nilai
    TableColumn<StudentResult, String> scoreCol = 
        new TableColumn<>("Score");
    
    // Load hasil
    loadResults(exam, resultsTable);
}
```

## 8. Helper Methods

### 8.1 Load Data
```java
private void loadResults(Exam exam, TableView<StudentResult> table) {
    studentAnswers.forEach((studentId, examAnswers) -> {
        List<Answer> answers = examAnswers.get(exam.getExamId());
        if (answers != null && !answers.isEmpty()) {
            float score = answers.get(0).getTotalScore();
            table.getItems().add(new StudentResult(studentId, score));
        }
    });
}
```

## 9. Inner Classes

### 9.1 Data Classes
```java
private static class ExamEntry {
    final Exam exam;
    ExamEntry(Exam exam) { this.exam = exam; }
}

private static class StudentResult {
    final String studentId;
    final float score;
    StudentResult(String studentId, float score) {
        this.studentId = studentId;
        this.score = score;
    }
}
```

## 10. Integrasi dengan Komponen Lain

### 10.1 Dengan ExamSystem
- Mengakses dan mengelola daftar ujian
- Melihat status publikasi ujian
- Mengelola soal-soal ujian

### 10.2 Dengan Student System
- Melihat jawaban mahasiswa
- Memberikan nilai
- Memonitor aktivitas mahasiswa

### 10.3 Dengan Logging System
- Melihat log aktivitas mahasiswa
- Mendeteksi kecurangan
- Monitoring real-time

## 11. Fitur Keamanan

### 11.1 Validasi Input
- Validasi nilai (0-100)
- Validasi format input
- Pencegahan input invalid

### 11.2 Monitoring
- Log aktivitas mahasiswa
- Deteksi perilaku mencurigakan
- Tracking waktu ujian

## 12. Best Practices

1. **Separation of Concerns**
   - UI terpisah dari logika bisnis
   - Modular design
   - Clean code principles

2. **Error Handling**
   - Try-catch untuk input numerik
   - Validasi data
   - Feedback yang jelas

3. **UI/UX**
   - Layout yang terstruktur
   - Feedback visual
   - Navigasi intuitif

LecturerDashboard.java berperan sebagai:
1. Interface manajemen ujian
2. Sistem penilaian
3. Monitoring aktivitas
4. Pengelolaan hasil
5. Kontrol keamanan

Dengan struktur ini, LecturerDashboard.java menjadi pusat kontrol bagi dosen untuk mengelola seluruh aspek ujian online.