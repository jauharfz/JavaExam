# Panduan Lengkap ExamSystemUI.java - Sistem Ujian Online

## 1. Overview Sistem
ExamSystemUI.java adalah kelas utama yang berfungsi sebagai entry point aplikasi ujian online. Kelas ini mengextend `Application` dari JavaFX untuk membuat antarmuka grafis.

### 1.1 Atribut Utama
```java
public class ExamSystemUI extends Application {
    // Menyimpan data user (mahasiswa & dosen)
    private static final Map<String, User> users = new HashMap<>();
    
    // Menyimpan daftar ujian
    private static final Map<String, Exam> exams = new HashMap<>();
    
    // Menyimpan jawaban mahasiswa
    private static final Map<String, Map<String, List<Answer>>> studentAnswers = new HashMap<>();
    
    // Menyimpan log aktivitas ujian
    private static final Map<String, List<KeyLogEntry>> examLogs = new HashMap<>();
}
```

### 1.2 Struktur Data
1. **users Map**:
   - Key: username (String)
   - Value: objek User (Student/Lecturer)
   ```java
   users.put("student", new Student("S001", "student", "pass123"));
   users.put("lecturer", new Lecturer("L001", "lecturer", "pass123"));
   ```

2. **exams Map**:
   - Key: examId (String)
   - Value: objek Exam
   ```java
   exams.put(exam.getExamId(), exam);
   ```

3. **studentAnswers Map** (Nested):
   - Key Level 1: studentId
   - Key Level 2: examId
   - Value: List jawaban
   ```java
   studentAnswers.get(studentId).get(examId) // mendapatkan jawaban
   ```

## 2. Inisialisasi Aplikasi

### 2.1 Method Start
```java
@Override
public void start(Stage primaryStage) {
    initializeData();      // Inisialisasi data awal
    setStageIcon(primaryStage);  // Set icon aplikasi
    showLoginScreen(primaryStage); // Tampilkan login screen
}
```

### 2.2 Inisialisasi Data
```java
private void initializeData() {
    // Inisialisasi Users
    users.put("student", new Student("S001", "student", "pass123"));
    users.put("student2", new Student("S002", "student2", "pass123"));
    users.put("lecturer", new Lecturer("L001", "lecturer", "pass123"));

    // Inisialisasi Exam
    Exam exam = new Exam("E001", "Programming Basics");
    addSampleQuestions(exam);
    exam.publish();
    exams.put(exam.getExamId(), exam);
}
```

## 3. Komponen UI Login

### 3.1 Login Screen Creation
```java
private void showLoginScreen(Stage stage) {
    VBox loginBox = createLoginBox(stage);
    Scene scene = new Scene(loginBox, 400, 300);
    stage.setTitle("Exam System Login");
    stage.setScene(scene);
    stage.show();
}
```

### 3.2 Login Box Components
```java
private VBox createLoginBox(Stage stage) {
    // Container utama
    VBox loginBox = new VBox(15);
    loginBox.setAlignment(Pos.CENTER);
    loginBox.setPadding(new Insets(20));
    
    // Components
    Label titleLabel = new Label("Exam System Login");
    TextField usernameField = new TextField();
    PasswordField passwordField = new PasswordField();
    Button loginButton = new Button("Login");
    Label statusLabel = new Label("");
    
    // Styling
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
}
```

## 4. Login Logic

### 4.1 Handle Login
```java
private void handleLogin(Stage stage, String username, String password, Label statusLabel) {
    User user = users.get(username);
    if (user != null && user.validatePassword(password)) {
        // Redirect berdasarkan tipe user
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

## 5. Manajemen Ujian

### 5.1 Inisialisasi Soal
```java
private void addSampleQuestions(Exam exam) {
    String[][] questions = {
        {"What is Java?", "A. Coffee", "B. Programming Language", "C. Island", "B"},
        {"Which is not a primitive type?", "A. int", "B. String", "C. boolean", "B"},
        // ... more questions
    };

    for (int i = 0; i < questions.length; i++) {
        Question q = new Question("Q" + (i + 1), questions[i][0], 20, "MULTIPLE_CHOICE");
        // Tambah opsi jawaban
        for (int j = 1; j <= 3; j++) {
            q.addOption(questions[i][j]);
        }
        q.setCorrectAnswer(questions[i][4]);
        exam.addQuestion(q);
    }
}
```

## 6. Dashboard Management

### 6.1 Student Dashboard
```java
private void showStudentDashboard(Stage stage, Student student) {
    StudentDashboard dashboard = new StudentDashboard(
        stage, student, exams, studentAnswers, examLogs);
    setStageIcon(stage);
    dashboard.show();
}
```

### 6.2 Lecturer Dashboard
```java
private void showLecturerDashboard(Stage stage, Lecturer lecturer) {
    LecturerDashboard dashboard = new LecturerDashboard(
        stage, lecturer, exams, studentAnswers, examLogs);
    setStageIcon(stage);
    dashboard.show();
}
```

## 7. Integrasi dengan Komponen Lain

### 7.1 Hubungan dengan User System
- Mengelola autentikasi user (Student/Lecturer)
- Validasi password melalui `User.validatePassword()`
- Redirect ke dashboard sesuai tipe user

### 7.2 Hubungan dengan Exam System
- Menyimpan dan mengelola daftar ujian
- Inisialisasi soal-soal ujian
- Mengatur status publikasi ujian

### 7.3 Hubungan dengan Answer System
- Menyimpan jawaban mahasiswa
- Mengakses hasil ujian untuk penilaian
- Menyediakan akses ke jawaban untuk dashboard

### 7.4 Hubungan dengan Logging System
- Menyimpan log aktivitas ujian
- Monitoring aktivitas mencurigakan
- Integrasi dengan ExamKeyLogger

## 8. Best Practices & Keamanan

### 8.1 Data Management
- Menggunakan `static final` untuk data storage
- Implementasi getter yang aman untuk data sensitif
- Validasi input sebelum pemrosesan

### 8.2 UI/UX
- Pesan error yang informatif
- Styling yang konsisten
- Responsif terhadap input user

### 8.3 Keamanan
- Validasi password
- Pemisahan role (Student/Lecturer)
- Logging aktivitas mencurigakan

## 9. Kesimpulan
ExamSystemUI.java berperan sebagai:
1. Entry point aplikasi
2. Controller utama sistem
3. Manager autentikasi
4. Koordinator antar komponen
5. UI/UX manager

Dengan struktur ini, ExamSystemUI.java menjadi backbone dari sistem ujian online, menghubungkan semua komponen dan mengatur alur aplikasi secara keseluruhan.