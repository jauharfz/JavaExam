# Analisis Student.java - Fundamental Guide

## 1. Class Overview
```java
public class Student extends User {
    private Map<String, List<Answer>> examAnswers = new HashMap<>();
}
```

### Karakteristik Penting:
1. **Inheritance**:
    - Extends `User` class
    - Mewarisi atribut dan method dasar user (userId, username, password)

2. **Data Structure**:
   ```java
   private Map<String, List<Answer>> examAnswers = new HashMap<>();
   ```
    - Menyimpan jawaban ujian dalam struktur Map
    - Key: examId (String)
    - Value: List jawaban untuk ujian tersebut

## 2. Constructor dan Methods

### Constructor
```java
public Student(String userId, String username, String password) {
    super(userId, username, password);
}
```
- Memanggil constructor parent class (User)
- Menginisialisasi data dasar student

### Core Methods

1. **Submit Answer**:
   ```java
   public void submitAnswer(Answer answer) {
       if (answer.validate()) {
           answer.submit();
           examAnswers.computeIfAbsent(answer.getExamId(), k -> new ArrayList<>())
                   .add(answer);
       }
   }
   ```
    - Memvalidasi dan menyimpan jawaban
    - Menggunakan `computeIfAbsent` untuk handling list creation
    - Otomatis membuat list baru jika examId belum ada

2. **Exam Completion Check**:
   ```java
   public boolean hasCompletedExam(String examId) {
       return examAnswers.containsKey(examId);
   }
   ```
    - Mengecek apakah student sudah menyelesaikan ujian tertentu

3. **Get Exam Answers**:
   ```java
   public List<Answer> getExamAnswers(String examId) {
       return examAnswers.getOrDefault(examId, new ArrayList<>());
   }
   ```
    - Mengambil jawaban untuk ujian tertentu
    - Returns empty list jika tidak ada jawaban

## 3. Hubungan dengan Komponen Lain

### 1. Interaksi dengan Answer
```java
// Di StudentDashboard.java
Answer studentAnswer = new Answer(student.getUserId(),
    exam.getExamId(), question.getQuestionId(), answer);
student.submitAnswer(studentAnswer);
```
- Menerima dan menyimpan jawaban ujian
- Terhubung dengan sistem penilaian

### 2. Interaksi dengan Exam
```java
// Di Exam.java
public boolean isStudentEligible(Student student) {
    if (!isPublished()) return false;
    Map<String, List<Answer>> studentExams = ExamSystemUI.getStudentAnswers()
        .get(student.getUserId());
    return studentExams == null || !studentExams.containsKey(examId);
}
```
- Mengecek eligibilitas untuk mengikuti ujian
- Mencegah pengulangan ujian yang sama

## 4. Best Practices Penggunaan

1. **Membuat Instance Student**:
   ```java
   Student student = new Student("S001", "john_doe", "password123");
   ```

2. **Menangani Jawaban**:
   ```java
   // Submitting single answer
   Answer answer = new Answer(studentId, examId, questionId, "B");
   student.submitAnswer(answer);

   // Checking completion
   if (!student.hasCompletedExam(examId)) {
       // Allow student to take exam
   }
   ```

3. **Mengakses Jawaban**:
   ```java
   List<Answer> examAnswers = student.getExamAnswers(examId);
   for (Answer answer : examAnswers) {
       // Process answers
   }
   ```

## 5. Tips Pengembangan

1. **Validasi Tambahan**:
   ```java
   public void submitAnswer(Answer answer) {
       // Add additional validation
       if (answer == null || answer.getExamId() == null) {
           throw new IllegalArgumentException("Invalid answer");
       }
       // Existing code...
   }
   ```

2. **Tracking Progress**:
   ```java
   public double getExamProgress(String examId) {
       List<Answer> answers = examAnswers.get(examId);
       if (answers == null) return 0.0;
       return (double) answers.size() / totalQuestions;
   }
   ```

3. **Performance Optimization**:
   ```java
   // Menggunakan ConcurrentHashMap untuk thread safety
   private Map<String, List<Answer>> examAnswers = 
       new ConcurrentHashMap<>();
   ```

## 6. Keamanan dan Validasi

1. **Answer Validation**:
   ```java
   private boolean validateAnswer(Answer answer) {
       return answer != null && 
           answer.getContent() != null && 
           !answer.getContent().isEmpty();
   }
   ```

2. **Exam Access Control**:
   ```java
   public boolean canAccessExam(String examId) {
       // Check if student is eligible
       // Check if exam is published
       // Check if exam is not already completed
   }
   ```

3. **Data Protection**:
   ```java
   public List<Answer> getExamAnswers(String examId) {
       // Return defensive copy
       return new ArrayList<>(
           examAnswers.getOrDefault(examId, new ArrayList<>())
       );
   }
   ```

Kode Student.java ini merupakan komponen penting dalam sistem ujian, menangani manajemen jawaban siswa dan interaksi dengan komponen lain dalam sistem. Desainnya memungkinkan tracking jawaban yang efisien dan integrasi yang baik dengan sistem penilaian.