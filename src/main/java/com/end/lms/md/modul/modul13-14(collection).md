Mari saya jelaskan penggunaan Collection (HashMap dan List) dalam kode dengan detail:

### 1. Penggunaan HashMap

Di kelas `ExamSystemUI.java`, terdapat beberapa HashMap yang digunakan untuk menyimpan data:

```java
private static final Map<String, User> users = new HashMap<>();
private static final Map<String, Exam> exams = new HashMap<>();
private static final Map<String, Map<String, List<Answer>>> studentAnswers = new HashMap<>();
private static final Map<String, List<KeyLogEntry>> examLogs = new HashMap<>();
```

Penjelasan:
- `users`: Menyimpan data user dengan key berupa username dan value berupa objek User
- `exams`: Menyimpan data ujian dengan key berupa examId dan value berupa objek Exam
- `studentAnswers`: HashMap nested yang menyimpan jawaban siswa:
    - Key level 1: studentId
    - Key level 2: examId
    - Value: List jawaban siswa
- `examLogs`: Menyimpan log aktivitas ujian dengan key berupa kombinasi studentId_examId dan value berupa List log

### 2. Penggunaan List

1. Di kelas `Exam.java`:
```java
private final List<Question> questions;
```
- Menyimpan daftar pertanyaan dalam sebuah ujian
- Diinisialisasi sebagai ArrayList di constructor: `this.questions = new ArrayList<>();`

2. Di kelas `Student.java`:
```java
private Map<String, List<Answer>> examAnswers = new HashMap<>();
```
- List digunakan untuk menyimpan kumpulan jawaban siswa untuk satu ujian

3. Di kelas `ExamKeyLogger.java`:
```java
private final List<KeyLogEntry> keyLogs;
```
- Menyimpan daftar log aktivitas keyboard saat ujian
- Diinisialisasi sebagai ArrayList: `this.keyLogs = new ArrayList<>();`

4. Di kelas `ExamSession.java`:
```java
private final List<String> activeStudentIds;
```
- Menyimpan daftar ID siswa yang sedang aktif dalam sesi ujian
- Diinisialisasi sebagai ArrayList di constructor

### Penggunaan Collection Methods

1. Operasi pada List:
```java
questions.add(question); // menambah question
questions.stream() // menggunakan stream untuk filter
keyLogs.add(entry); // menambah log entry
activeStudentIds.add(studentId); // menambah student ID
```

2. Operasi pada Map:
```java
users.get(username); // mengambil user berdasarkan username
exams.put(exam.getExamId(), exam); // menyimpan exam baru
studentAnswers.computeIfAbsent(studentId, k -> new HashMap<>()); // membuat map baru jika belum ada
examLogs.put(logKey, session.getStudentCheatingLogs(studentId)); // menyimpan log
```

Penggunaan Collection Framework ini memberikan beberapa keuntungan:
1. Fleksibilitas ukuran data yang bisa disimpan (dinamis)
2. Built-in methods untuk manipulasi data
3. Type safety dengan penggunaan generics
4. Kemudahan dalam iterasi dan pencarian data
5. Struktur data yang optimal untuk use case masing-masing (HashMap untuk lookup cepat, List untuk data berurutan)