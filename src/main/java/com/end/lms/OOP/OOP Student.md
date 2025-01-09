Saya akan menjelaskan LecturerDashboard.java secara detail dengan fokus pada konsep OOP:

# LecturerDashboard.java Analysis

## 1. Encapsulation

### Private Fields:
```java
private final Lecturer lecturer;
private final Stage stage;
private final Map<String, Exam> exams;
private final Map<String, Map<String, List<Answer>>> studentAnswers;
private final Map<String, List<KeyLogEntry>> examLogs;
```
- Data hiding dengan keyword `private`
- `final` untuk immutability
- Kompleks data structure yang terenkapsulasi

### Constructor dengan Parameter Encapsulation:
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
- Controlled access ke internal state
- Dependency injection pattern
- Initialization protection

## 2. Collection Framework Usage

### Complex Data Structures:
```java
// Nested Collections
private final Map<String, Map<String, List<Answer>>> studentAnswers;
private final Map<String, List<KeyLogEntry>> examLogs;

// Collection Operations
Map<String, Answer> studentTotalScores = new HashMap<>();
studentAnswers.forEach((studentId, examAnswers) -> {
    List<Answer> answers = examAnswers.get(exam.getExamId());
    if (answers != null && !answers.isEmpty()) {
        Answer firstAnswer = answers.get(0);
        studentTotalScores.put(studentId, firstAnswer);
    }
});
```
- Multiple nested collections
- Stream API usage
- Lambda expressions dengan collections

## 3. Inner Classes (Encapsulation & Data Structure)

```java
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
    // Constructor
}
```
- Data encapsulation dalam inner classes
- Immutable objects dengan `final`
- Support untuk UI components

## 4. Method Organization & Relationships

### Tab Creation Methods:
```java
private Tab createExamManagementTab() {
    // Creates exam management interface
}

private Tab createActivityLogsTab() {
    // Creates activity monitoring interface
}
```
- Modular design
- Separation of concerns
- Clear method responsibilities

### Score Management:
```java
private void showScoreEditor(Exam exam) {
    // Complex UI and data management
    TableView<ScoreEntry> scoreTable = new TableView<>();
    // ... setup columns and data
    
    Button autoGradeBtn = new Button("Auto Grade");
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
}
```
- Complex data processing
- Event handling
- UI update management

## 5. Anti-Cheating System Integration

```java
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
```
- Monitoring system
- Log management
- Security implementation

## 6. UI Component Management

### Dashboard Creation:
```java
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
    // ... more UI setup
}
```
- Component hierarchy
- Layout management
- Event handling

## 7. Data Management & Processing

### Grade Processing:
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
- Data transformation
- Collection processing
- UI data binding

## 8. Hubungan dengan Class Lain

1. **Lecturer Class**:
    - Extends User
    - Provides authentication & identification
    - Used for access control

2. **Exam Class**:
    - Manages exam content & structure
    - Provides scoring functionality
    - Controls exam state (published/draft)

3. **Answer Class**:
    - Stores student responses
    - Manages grading state
    - Provides validation

4. **KeyLogEntry Class**:
    - Records student activities
    - Supports anti-cheating system
    - Provides timestamp functionality

## 9. Polymorphic Behavior

```java
TableColumn<ScoreEntry, String> scoreCol = new TableColumn<>("Total Score");
scoreCol.setCellFactory(col -> new TableCell<ScoreEntry, String>() {
    private final TextField textField = new TextField();
    
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        // Custom cell rendering logic
    }
});
```
- Custom cell rendering
- Method overriding
- Anonymous class implementation

## 10. Error Handling & Validation

```java
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
```
- Exception handling
- Input validation
- State recovery

Kode ini menunjukkan implementasi kompleks dari prinsip OOP dengan:
- Strong encapsulation
- Effective use of collections
- Clear separation of concerns
- Robust error handling
- Comprehensive UI management
- Secure data access
- Modular design patterns