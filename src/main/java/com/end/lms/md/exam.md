# Analisis Exam.java - Fundamental Guide

## 1. Class Overview
```java
public class Exam {
    private String examId;
    private String title;
    private boolean isPublished;
    private List<Question> questions;
}
```

### Karakteristik Penting:
1. **Atribut Utama**:
    - `examId`: Identifikasi unik ujian
    - `title`: Judul ujian
    - `isPublished`: Status publikasi ujian
    - `questions`: Daftar pertanyaan dalam ujian

2. **Data Structure**:
   ```java
   private List<Question> questions = new ArrayList<>();
   ```
    - Menggunakan ArrayList untuk menyimpan pertanyaan
    - Memungkinkan penambahan pertanyaan secara dinamis

## 2. Constructor dan Methods Utama

### Constructor
```java
public Exam(String examId, String title) {
    this.examId = examId;
    this.title = title;
    this.isPublished = false;
    this.questions = new ArrayList<>();
}
```
- Inisialisasi ujian baru
- Status awal: tidak dipublikasikan
- List pertanyaan kosong

### Core Methods

1. **Manajemen Pertanyaan**:
   ```java
   public void addQuestion(Question question) {
       questions.add(question);
   }
   ```
    - Menambahkan pertanyaan ke ujian
    - Digunakan saat setup ujian

2. **Publikasi Ujian**:
   ```java
   public void publish() {
       if (!questions.isEmpty()) {
           this.isPublished = true;
           System.out.println("Exam " + title + " has been published.");
       } else {
           System.out.println("Cannot publish exam without questions.");
       }
   }
   ```
    - Mempublikasikan ujian jika ada pertanyaan
    - Validasi keberadaan pertanyaan

3. **Perhitungan Nilai**:
   ```java
   public float calculateScore(List<Answer> answers) {
       if (answers == null || answers.isEmpty())
           return 0;

       float totalScore = 0;
       for (Answer answer : answers) {
           Question question = findQuestionForAnswer(answer);
           if (question != null && question.validateAnswer(answer.getContent())) {
               totalScore += 20; // Setiap pertanyaan bernilai 20 poin
           }
       }
       return totalScore;
   }
   ```
    - Menghitung total nilai berdasarkan jawaban
    - Menggunakan validasi dari Question

## 3. Hubungan dengan Komponen Lain

### 1. Interaksi dengan Question
```java
private Question findQuestionForAnswer(Answer answer) {
    return questions.stream()
            .filter(q -> q.getQuestionId().equals(answer.getQuestionId()))
            .findFirst()
            .orElse(null);
}
```
- Mencari pertanyaan berdasarkan jawaban
- Menggunakan Java Stream untuk pencarian efisien

### 2. Interaksi dengan Student
```java
public boolean isStudentEligible(Student student) {
    if (!isPublished()) return false;

    Map<String, List<Answer>> studentExams = ExamSystemUI.getStudentAnswers()
        .get(student.getUserId());
    return studentExams == null || !studentExams.containsKey(examId);
}
```
- Mengecek eligibilitas siswa
- Mencegah pengulangan ujian

### 3. Interaksi dengan LecturerDashboard
```java
// Di LecturerDashboard.java
private void autoGradeExam(Exam exam, List<Answer> answers) {
    float score = exam.calculateScore(answers);
    answers.forEach(answer -> answer.setTotalScore(score));
}
```
- Digunakan untuk penilaian otomatis
- Menerapkan nilai ke jawaban siswa

## 4. Best Practices Penggunaan

1. **Membuat Ujian Baru**:
   ```java
   Exam exam = new Exam("E001", "Programming Basics");
   Question q1 = new Question("Q1", "What is Java?", 20, "MULTIPLE_CHOICE");
   exam.addQuestion(q1);
   exam.publish();
   ```

2. **Menghitung Nilai**:
   ```java
   List<Answer> studentAnswers = student.getExamAnswers(examId);
   float score = exam.calculateScore(studentAnswers);
   ```

3. **Validasi Publikasi**:
   ```java
   if (exam.isPublished()) {
       // Tampilkan ujian ke siswa
   } else {
       // Tampilkan pesan error
   }
   ```

## 5. Tips Pengembangan

1. **Validasi Input**:
   ```java
   public void addQuestion(Question question) {
       if (question == null) {
           throw new IllegalArgumentException("Question cannot be null");
       }
       questions.add(question);
   }
   ```

2. **Score Management**:
   ```java
   private int calculateQuestionScore(Question question) {
       return question.getDifficulty() * 10; // Basis skor dari tingkat kesulitan
   }
   ```

3. **Time Management**:
   ```java
   private LocalDateTime startTime;
   private int durationMinutes;
   
   public void setDuration(int minutes) {
       this.durationMinutes = minutes;
   }
   ```

## 6. Keamanan dan Validasi

1. **Question Access**:
   ```java
   public List<Question> getQuestions() {
       return new ArrayList<>(questions); // Defensive copy
   }
   ```

2. **Publication Control**:
   ```java
   public void unpublish() {
       if (!hasActiveStudents()) {
           this.isPublished = false;
       }
   }
   ```

3. **Answer Validation**:
   ```java
   private boolean isValidAnswer(Answer answer) {
       return answer != null && 
              questions.stream().anyMatch(q -> 
                  q.getQuestionId().equals(answer.getQuestionId()));
   }
   ```

Exam.java adalah komponen kunci dalam sistem ujian yang mengelola semua aspek ujian, termasuk pertanyaan, penilaian, dan status publikasi. Class ini terintegrasi dengan baik dengan komponen lain seperti Question, Answer, dan dashboard untuk memberikan fungsionalitas ujian yang lengkap.