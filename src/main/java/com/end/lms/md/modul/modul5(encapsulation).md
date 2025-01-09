Mari saya analisis implementasi konsep Encapsulation dalam project ini secara detail:

## 1. Access Modifier Implementation

### Private Fields
Beberapa contoh penggunaan private untuk data hiding:

1. Di kelas `User`:
```java
public abstract class User {
    private String userId;
    private String username; 
    private String password;
}
```

2. Di kelas `Answer`:
```java
public class Answer {
    private String examId;
    private String content;
    private String questionId;
    private boolean isGraded;
    private float totalScore;
}
```

3. Di kelas `Question`:
```java
public class Question {
    private String questionId;
    private String content;
    private String type;
    private List<String> options;
    private String correctAnswer;
}
```

### Public Methods
Contoh public methods untuk mengakses private fields:

1. Di kelas `User`:
```java
public String getUserId() {
    return userId;
}

public String getUsername() {
    return username;
}

public boolean validatePassword(String inputPassword) {
    return this.password.equals(inputPassword);
}
```

2. Di kelas `Answer`:
```java
public String getQuestionId() {
    return questionId;
}

public float getScore() {
    return totalScore;
}

public void setTotalScore(float score) {
    this.totalScore = score;
    this.isGraded = true;
}
```

## 2. Getter dan Setter Methods

### Getter Methods
Contoh implementasi getter methods:

1. Di kelas `Exam`:
```java
public String getExamId() {
    return examId;
}

public String getTitle() {
    return title;
}

public boolean isPublished() {
    return isPublished;
}

public List<Question> getQuestions() {
    return new ArrayList<>(questions); // Return copy untuk encapsulation yang lebih baik
}
```

2. Di kelas `Question`:
```java
public String getQuestionId() {
    return questionId;
}

public String getContent() {
    return content;
}

public List<String> getOptions() {
    return new ArrayList<>(options); // Return copy untuk encapsulation yang lebih baik
}
```

### Setter Methods
Contoh implementasi setter methods:

1. Di kelas `Answer`:
```java
public void setTotalScore(float score) {
    this.totalScore = score;
    this.isGraded = true;
}
```

2. Di kelas `Question`:
```java
public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
}
```

## 3. Data Hiding & Encapsulation Patterns

1. **Immutable Fields**
   Di kelas `Exam`:
```java
private final String examId;
private final String title;
private final List<Question> questions;
```
Fields ini dibuat final untuk mencegah modifikasi setelah inisialisasi.

2. **Defensive Copying**
   Di beberapa getter methods, return value berupa copy dari collection untuk mencegah modifikasi langsung:
```java
public List<Question> getQuestions() {
    return new ArrayList<>(questions);
}

public List<String> getOptions() {
    return new ArrayList<>(options);
}
```

3. **Validation in Setters**
   Di kelas `ExamSession`:
```java
public void startSession() {
    LocalDateTime startTime = LocalDateTime.now();
    this.isActive = true;
    System.out.println("Exam session started at: " + startTime);
}
```

## 4. Keuntungan Implementasi

1. **Data Security**
- Private fields mencegah akses langsung dari luar kelas
- Validasi data dapat dilakukan di setter methods
- Kontrol akses data melalui public methods

2. **Flexibility**
- Implementasi internal dapat diubah tanpa mempengaruhi kode yang menggunakan kelas
- Business logic dapat ditambahkan di getter/setter methods

3. **Maintainability**
- Kode lebih terstruktur dan mudah dimaintain
- Perubahan implementasi tidak mempengaruhi interface publik

## 5. Saran Pengembangan

1. Menambahkan validasi yang lebih ketat di setter methods
2. Implementasi immutability untuk objek-objek yang tidak perlu dimodifikasi
3. Menggunakan builder pattern untuk objek kompleks
4. Menambahkan logging untuk tracking perubahan state

Project ini sudah mengimplementasikan konsep encapsulation dengan baik, terutama dalam hal data hiding dan access control. Namun masih ada ruang untuk peningkatan dalam hal validasi data dan immutability.