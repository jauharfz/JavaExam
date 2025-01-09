# Analisis Answer.java - Fundamental Guide

## 1. Class Overview
```java
public class Answer {
    private String examId;
    private String content;
    private String questionId;
    private boolean isGraded;
    private float totalScore;
}
```

### Karakteristik Penting:
1. **Atribut Utama**:
    - `examId`: Identifikasi ujian yang dijawab
    - `content`: Isi jawaban siswa
    - `questionId`: Identifikasi pertanyaan yang dijawab
    - `isGraded`: Status penilaian jawaban
    - `totalScore`: Nilai yang diberikan

## 2. Constructor dan Methods

### Constructor
```java
public Answer(String studentId, String examId, String questionId, String content) {
    this.questionId = questionId;
    this.content = content;
    this.isGraded = false;
    this.totalScore = 0.0f;
    this.examId = examId;
}
```
- Inisialisasi jawaban baru
- Status awal: belum dinilai (isGraded = false)
- Nilai awal: 0.0

### Core Methods

1. **Validasi Jawaban**:
   ```java
   public boolean validate() {
       return content != null && !content.trim().isEmpty();
   }
   ```
    - Memastikan jawaban tidak kosong
    - Digunakan sebelum submit

2. **Submit Jawaban**:
   ```java
   public void submit() {
       validate();
   }
   ```
    - Memproses pengiriman jawaban
    - Melakukan validasi sebelum submit

3. **Manajemen Nilai**:
   ```java
   public void setTotalScore(float score) {
       this.totalScore = score;
       this.isGraded = true;
   }
   ```
    - Mengatur nilai jawaban
    - Mengubah status menjadi sudah dinilai

## 3. Hubungan dengan Komponen Lain

### 1. Interaksi dengan Question
```java
// Di Question.java
public boolean validateAnswer(String answer) {
    if (type.equals("MULTIPLE_CHOICE")) {
        return answer != null && answer.equals(correctAnswer);
    }
    return answer != null && !answer.trim().isEmpty();
}
```
- Question memvalidasi konten jawaban
- Menentukan kebenaran jawaban

### 2. Interaksi dengan Student
```java
// Di Student.java
public void submitAnswer(Answer answer) {
    if (answer.validate()) {
        answer.submit();
        examAnswers.computeIfAbsent(answer.getExamId(), k -> new ArrayList<>())
                .add(answer);
    }
}
```
- Student menyimpan dan mengelola jawaban
- Menggunakan validasi Answer

### 3. Interaksi dengan Lecturer
```java
// Di Lecturer.java
public void gradeExam(Answer answer, float score) {
    answer.setTotalScore(score);
}
```
- Lecturer memberikan nilai pada jawaban
- Mengubah status jawaban menjadi dinilai

## 4. Best Practices Penggunaan

1. **Membuat Jawaban Baru**:
   ```java
   Answer answer = new Answer(studentId, "E001", "Q1", "B");
   if (answer.validate()) {
       student.submitAnswer(answer);
   }
   ```

2. **Penilaian Jawaban**:
   ```java
   answer.setTotalScore(85.0f);
   if (answer.isGraded()) {
       System.out.println("Nilai: " + answer.getTotalScore());
   }
   ```

3. **Mengakses Informasi Jawaban**:
   ```java
   String examId = answer.getExamId();
   String content = answer.getContent();
   boolean isGraded = answer.isGraded();
   ```

## 5. Tips Pengembangan

1. **Validasi Input yang Lebih Kuat**:
   ```java
   public boolean validate() {
       if (content == null || content.trim().isEmpty()) {
           return false;
       }
       if (examId == null || questionId == null) {
           return false;
       }
       return true;
   }
   ```

2. **Score Range Validation**:
   ```java
   public void setTotalScore(float score) {
       if (score >= 0 && score <= 100) {
           this.totalScore = score;
           this.isGraded = true;
       } else {
           throw new IllegalArgumentException("Score must be between 0 and 100");
       }
   }
   ```

3. **Timestamp Tracking**:
   ```java
   private LocalDateTime submittedAt;
   private LocalDateTime gradedAt;
   
   public void submit() {
       if (validate()) {
           this.submittedAt = LocalDateTime.now();
       }
   }
   ```

## 6. Keamanan dan Validasi

1. **Content Validation**:
   ```java
   public void setContent(String content) {
       if (content == null || content.trim().isEmpty()) {
           throw new IllegalArgumentException("Answer content cannot be empty");
       }
       this.content = content;
   }
   ```

2. **Immutable Fields**:
   ```java
   public String getExamId() {
       return examId; // Cannot be modified after creation
   }
   ```

3. **Score Protection**:
   ```java
   private void validateScore(float score) {
       if (score < 0 || score > 100) {
           throw new IllegalArgumentException("Invalid score range");
       }
   }
   ```

Answer.java adalah komponen penting yang menangani jawaban siswa dalam sistem ujian. Class ini bertanggung jawab untuk menyimpan, memvalidasi, dan mengelola nilai jawaban siswa. Desainnya memungkinkan integrasi yang baik dengan komponen sistem lainnya seperti Question, Student, dan Lecturer.

Kelas ini memainkan peran kunci dalam:
1. Menyimpan jawaban siswa
2. Memvalidasi jawaban sebelum submit
3. Mengelola status penilaian
4. Menyediakan informasi untuk penilaian

Dengan struktur yang ada, Answer.java memberikan dasar yang solid untuk sistem penilaian ujian yang terorganisir dan dapat diandalkan.