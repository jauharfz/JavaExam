Baik, saya akan menjelaskan ExamSystemUI.java secara detail dengan fokus pada konsep OOP:

# ExamSystemUI.java Analysis

## 1. Encapsulation

### Private Fields (Data Hiding):
```java
private static final Map<String, User> users = new HashMap<>();
private static final Map<String, Exam> exams = new HashMap<>();
private static final Map<String, Map<String, List<Answer>>> studentAnswers = new HashMap<>();
private static final Map<String, List<KeyLogEntry>> examLogs = new HashMap<>();
private static final String ICON_PATH = "/com/end/lms/icon.png";
```
- Menggunakan `private` untuk menyembunyikan data internal
- `static final` menunjukkan data bersifat konstan dan shared
- Akses data hanya melalui method (getters/setters)

### Public Interface:
```java
public static Map<String, Map<String, List<Answer>>> getStudentAnswers() {
    return studentAnswers;
}
```
- Menyediakan akses terkontrol ke data internal
- Implementasi internal tersembunyi dari luar class

## 2. Inheritance

### Extends Application:
```java
public class ExamSystemUI extends Application {
```
- Mewarisi class `Application` dari JavaFX
- Mengimplementasikan method `start()` dari parent class
- Mendapatkan semua fungsionalitas GUI dari JavaFX

## 3. Polymorphism

### Method Overriding:
```java
@Override
public void start(Stage primaryStage) {
    initializeData();
    setStageIcon(primaryStage);
    showLoginScreen(primaryStage);
}
```
- Override method `start()` dari class `Application`
- Implementasi spesifik untuk aplikasi exam system

### Runtime Polymorphism:
```java
private void handleLogin(Stage stage, String username, String password, Label statusLabel) {
    User user = users.get(username);
    if (user != null && user.validatePassword(password)) {
        if (user instanceof Student) {
            showStudentDashboard(stage, (Student) user);
        } else {
            showLecturerDashboard(stage, (Lecturer) user);
        }
    }
}
```
- Penggunaan `instanceof` untuk pengecekan tipe runtime
- Different behavior berdasarkan tipe user (Student/Lecturer)

## 4. Collection Framework

### Map Usage:
```java
private static final Map<String, User> users = new HashMap<>();
private static final Map<String, Exam> exams = new HashMap<>();
private static final Map<String, Map<String, List<Answer>>> studentAnswers;
```
- Penggunaan nested collections
- `HashMap` untuk menyimpan data dengan key-value pairs
- Complex data structure dengan Map dalam Map

### List Usage:
```java
private static final Map<String, List<KeyLogEntry>> examLogs = new HashMap<>();
```
- `List` untuk menyimpan sequence of data
- Dynamic sizing dan flexible access

## 5. Method Relationships

### UI Flow Methods:
```java
private void showLoginScreen(Stage stage) {
    VBox loginBox = createLoginBox(stage);
    Scene scene = new Scene(loginBox, 400, 300);
    stage.setTitle("Exam System Login");
    stage.setScene(scene);
    stage.show();
}

private VBox createLoginBox(Stage stage) {
    // Creates login UI components
}
```
- Hierarchical method calls
- Separation of concerns antara logic dan UI

### Data Initialization:
```java
private void initializeData() {
    // Initialize users
    users.put("student", new Student("S001", "student", "pass123"));
    // Initialize exams
    Exam exam = new Exam("E001", "Programming Basics");
    addSampleQuestions(exam);
}

private void addSampleQuestions(Exam exam) {
    // Add questions to exam
}
```
- Method decomposition
- Modular design untuk maintenance

## 6. Event Handling

```java
loginButton.setOnAction(e -> handleLogin(stage, usernameField.getText(),
        passwordField.getText(), statusLabel));
```
- Lambda expressions untuk event handling
- Callback pattern implementation

## 7. Dependencies dengan Class Lain

```java
import com.end.lms.*;
```
- Menggunakan class `User`, `Student`, `Lecturer`, `Exam`
- Loose coupling melalui interfaces dan abstraction

## 8. UI Component Creation

```java
private VBox createLoginBox(Stage stage) {
    VBox loginBox = new VBox(15);
    loginBox.setAlignment(Pos.CENTER);
    loginBox.setPadding(new Insets(20));
    // ... component creation
}
```
- Factory pattern untuk UI components
- Consistent styling dan layout

## 9. Error Handling

```java
private void setStageIcon(Stage stage) {
    try {
        Image icon = new Image(Objects.requireNonNull(
            getClass().getResourceAsStream(ICON_PATH)));
        stage.getIcons().add(icon);
    } catch (Exception e) {
        System.err.println("Warning: Could not load application icon");
    }
}
```
- Exception handling
- Graceful degradation

## 10. Navigation Logic

```java
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
```
- Conditional navigation
- Type-based routing
- Error feedback

Kode ini menunjukkan implementasi solid dari prinsip-prinsip OOP dengan:
- Strong encapsulation
- Proper inheritance hierarchy
- Effective use of polymorphism
- Complex collection management
- Clear separation of concerns
- Modular design
- Robust error handling