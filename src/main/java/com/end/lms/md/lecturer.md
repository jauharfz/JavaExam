# Analisis Lecturer.java - Fundamental Guide

## 1. Class Overview
```java
public class Lecturer extends User {
    // Mewarisi atribut dari User: userId, username, password
}
```

### Karakteristik Penting:
1. **Inheritance**:
    - Extends `User` class
    - Mewarisi semua atribut dan method dasar dari User
    - Fokus pada fungsionalitas penilaian (grading)

2. **Core Responsibilities**:
    - Menilai ujian (manual grading)
    - Melakukan penilaian otomatis (auto grading)

## 2. Constructor dan Methods

### Constructor
```java
public Lecturer(String userId, String username, String password) {
    super(userId, username, password);
}
```
- Memanggil constructor parent class (User)
- Menginisialisasi data dasar lecturer

### Core Methods

1. **Manual Grading**:
   ```java
   public void gradeExam(Answer answer, float score) {
       answer.setTotalScore(score);
   }
   ```
    - Memberikan nilai secara manual ke jawaban siswa
    - Parameter:
        - `answer`: Jawaban yang akan dinilai
        - `score`: Nilai yang diberikan

2. **Auto Grading**:
   ```java
   public void autoGradeExam(Exam exam, List<Answer> answers) {
       float score = exam.calculateScore(answers);
       if (!answers.isEmpty()) {
           answers.get(0).setTotalScore(score);
       }
   }
   ```
    - Melakukan penilaian otomatis berdasarkan jawaban benar
    - Menggunakan `calculateScore` dari class Exam
    - Menyimpan nilai ke jawaban pertama

## 3. Hubungan dengan Komponen Lain

### 1. Interaksi dengan Answer
```java
// Di LecturerDashboard.java
lecturer.gradeExam(answer, newScore);
```
- Memberikan nilai pada jawaban siswa
- Mengubah status jawaban menjadi "graded"

### 2. Interaksi dengan Exam
```java
// Di LecturerDashboard.java
lecturer.autoGradeExam(exam, answers);
```
- Menggunakan logika penilaian dari Exam
- Menerapkan nilai otomatis ke jawaban siswa

### 3. Interaksi dengan LecturerDashboard
```java
// Di LecturerDashboard.java
private final Lecturer lecturer;
// ...
public LecturerDashboard(Stage stage, Lecturer lecturer, ...) {
    this.lecturer = lecturer;
    // ...
}
```
- Dashboard menggunakan lecturer untuk operasi penilaian
- Menampilkan informasi lecturer

## 4. Best Practices Penggunaan

1. **Manual Grading**:
   ```java
   // Contoh penggunaan gradeExam
   Answer studentAnswer = student.getExamAnswers(examId).get(0);
   lecturer.gradeExam(studentAnswer, 85.5f);
   ```

2. **Batch Auto Grading**:
   ```java
   // Menilai semua jawaban dalam satu ujian
   List<Answer> examAnswers = studentAnswers.get(examId);
   lecturer.autoGradeExam(exam, examAnswers);
   ```

3. **Validasi Nilai**:
   ```java
   public void gradeExam(Answer answer, float score) {
       if (score >= 0 && score <= 100) {
           answer.setTotalScore(score);
       } else {
           throw new IllegalArgumentException("Score must be between 0 and 100");
       }
   }
   ```

## 5. Tips Pengembangan

1. **Tambahan Method Validasi**:
   ```java
   private boolean isValidScore(float score) {
       return score >= 0 && score <= 100;
   }
   
   public void gradeExam(Answer answer, float score) {
       if (isValidScore(score)) {
           answer.setTotalScore(score);
       }
   }
   ```

2. **Batch Processing**:
   ```java
   public void gradeMultipleAnswers(List<Answer> answers, float score) {
       answers.forEach(answer -> gradeExam(answer, score));
   }
   ```

3. **Logging dan Tracking**:
   ```java
   public void gradeExam(Answer answer, float score) {
       answer.setTotalScore(score);
       System.out.println("Answer graded by " + getUsername() + 
           ": Score = " + score);
   }
   ```

## 6. Keamanan dan Validasi

1. **Score Validation**:
   ```java
   public void gradeExam(Answer answer, float score) {
       if (score < 0 || score > 100) {
           throw new IllegalArgumentException("Invalid score range");
       }
       answer.setTotalScore(score);
   }
   ```

2. **Null Checking**:
   ```java
   public void autoGradeExam(Exam exam, List<Answer> answers) {
       if (exam == null || answers == null) {
           throw new IllegalArgumentException("Exam and answers cannot be null");
       }
       // existing code...
   }
   ```

3. **Permission Checking**:
   ```java
   public boolean canGradeExam(String examId) {
       // Implement permission logic
       return true; // Placeholder
   }
   ```

Kode Lecturer.java ini merupakan komponen penting dalam sistem ujian yang menangani semua aspek penilaian, baik manual maupun otomatis. Class ini dirancang dengan fokus pada fungsionalitas penilaian dan terintegrasi dengan baik dengan komponen sistem lainnya seperti Answer, Exam, dan LecturerDashboard.