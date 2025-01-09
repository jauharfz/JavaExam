# Analisis Question.java - Fundamental Guide

## 1. Class Overview
```java
public class Question {
    private String questionId;
    private String content;
    private String type;
    private List<String> options;
    private String correctAnswer;
}
```

### Karakteristik Penting:
1. **Atribut Utama**:
    - `questionId`: Identifikasi unik pertanyaan
    - `content`: Isi pertanyaan
    - `type`: Tipe pertanyaan (saat ini mendukung "MULTIPLE_CHOICE")
    - `options`: List pilihan jawaban
    - `correctAnswer`: Jawaban benar untuk validasi

2. **Data Structure**:
   ```java
   private List<String> options = new ArrayList<>();
   ```
    - Menggunakan ArrayList untuk menyimpan opsi jawaban
    - Memungkinkan penambahan opsi secara dinamis

## 2. Constructor dan Methods

### Constructor
```java
public Question(String questionId, String content, int score, String type) {
    this.questionId = questionId;
    this.content = content;
    this.type = type;
    this.options = new ArrayList<>();
}
```
- Menginisialisasi pertanyaan baru
- Parameter `score` tidak digunakan (potential improvement)
- Membuat list opsi kosong

### Core Methods

1. **Add Option**:
   ```java
   public void addOption(String option) {
       options.add(option);
   }
   ```
    - Menambahkan opsi jawaban ke pertanyaan
    - Digunakan saat setup pertanyaan

2. **Validate Answer**:
   ```java
   public boolean validateAnswer(String answer) {
       if (type.equals("MULTIPLE_CHOICE")) {
           return answer != null && answer.equals(correctAnswer);
       }
       return answer != null && !answer.trim().isEmpty();
   }
   ```
    - Memvalidasi jawaban siswa
    - Berbeda berdasarkan tipe pertanyaan
    - Multiple choice: membandingkan dengan jawaban benar
    - Tipe lain: memastikan jawaban tidak kosong

3. **Getter Methods**:
   ```java
   public String getQuestionId() {
       return questionId;
   }

   public String getContent() {
       return content;
   }

   public List<String> getOptions() {
       return new ArrayList<>(options); // defensive copy
   }
   ```

## 3. Hubungan dengan Komponen Lain

### 1. Interaksi dengan Exam
```java
// Di Exam.java
public void addQuestion(Question question) {
    questions.add(question);
}

private Question findQuestionForAnswer(Answer answer) {
    return questions.stream()
            .filter(q -> q.getQuestionId().equals(answer.getQuestionId()))
            .findFirst()
            .orElse(null);
}
```
- Exam menyimpan dan mengelola koleksi Question
- Digunakan untuk penilaian jawaban

### 2. Interaksi dengan StudentDashboard
```java
// Di StudentDashboard.java
for (Question question : exam.getQuestions()) {
    VBox questionBox = new VBox(5);
    Label questionLabel = new Label(question.getContent());
    // ... setup UI untuk pertanyaan
    for (String option : question.getOptions()) {
        RadioButton rb = new RadioButton(option);
        // ... setup opsi jawaban
    }
}
```
- Menampilkan pertanyaan dan opsi di UI
- Mengatur interaksi user dengan pertanyaan

## 4. Best Practices Penggunaan

1. **Membuat Pertanyaan Baru**:
   ```java
   Question question = new Question("Q1", "What is Java?", 20, "MULTIPLE_CHOICE");
   question.addOption("A. Coffee");
   question.addOption("B. Programming Language");
   question.addOption("C. Island");
   question.setCorrectAnswer("B");
   ```

2. **Validasi Jawaban**:
   ```java
   String studentAnswer = "B";
   if (question.validateAnswer(studentAnswer)) {
       // Jawaban benar
       score += 20;
   }
   ```

3. **Mengakses Opsi**:
   ```java
   List<String> options = question.getOptions();
   for (String option : options) {
       // Tampilkan atau proses opsi
   }
   ```

## 5. Tips Pengembangan

1. **Tipe Pertanyaan Tambahan**:
   ```java
   public enum QuestionType {
       MULTIPLE_CHOICE,
       SHORT_ANSWER,
       ESSAY
   }
   ```

2. **Validasi Input**:
   ```java
   public void addOption(String option) {
       if (option == null || option.trim().isEmpty()) {
           throw new IllegalArgumentException("Option cannot be empty");
       }
       options.add(option);
   }
   ```

3. **Score Management**:
   ```java
   private int score;
   
   public int getScore() {
       return score;
   }
   
   public void setScore(int score) {
       if (score >= 0) {
           this.score = score;
       }
   }
   ```

## 6. Keamanan dan Validasi

1. **Option Validation**:
   ```java
   public void setCorrectAnswer(String correctAnswer) {
       if (!options.contains(correctAnswer)) {
           throw new IllegalArgumentException("Answer must be one of the options");
       }
       this.correctAnswer = correctAnswer;
   }
   ```

2. **Type Safety**:
   ```java
   public boolean isValidType(String type) {
       return type.equals("MULTIPLE_CHOICE") || 
              type.equals("SHORT_ANSWER");
   }
   ```

3. **Defensive Copying**:
   ```java
   public List<String> getOptions() {
       return new ArrayList<>(options); // Prevent external modification
   }
   ```

Question.java adalah komponen fundamental dalam sistem ujian yang menangani struktur dan logika pertanyaan. Class ini bertanggung jawab untuk menyimpan informasi pertanyaan, opsi jawaban, dan memvalidasi jawaban siswa. Desainnya memungkinkan fleksibilitas dalam tipe pertanyaan dan integrasi yang baik dengan komponen sistem lainnya.