# Analisis ExamSession.java - Fundamental Guide

## 1. Class Overview
```java
public class ExamSession {
    private boolean isActive;
    private final Map<String, ExamKeyLogger> studentKeyLoggers;
    private final List<String> activeStudentIds;
}
```

### Karakteristik Penting:
1. **Atribut Utama**:
    - `isActive`: Status sesi ujian (aktif/tidak)
    - `studentKeyLoggers`: Menyimpan logger untuk setiap siswa
    - `activeStudentIds`: Daftar ID siswa yang aktif

2. **Fungsi Utama**:
    - Mengelola sesi ujian
    - Memantau aktivitas siswa
    - Mencatat log kecurangan

## 2. Constructor dan Methods Utama

### Constructor
```java
public ExamSession() {
    this.studentKeyLoggers = new HashMap<>();
    this.activeStudentIds = new ArrayList<>();
    this.isActive = false;
}
```
- Inisialisasi sesi baru
- Menyiapkan struktur data untuk logging
- Status awal: tidak aktif

### Core Methods

1. **Manajemen Sesi**:
   ```java
   public void startSession() {
       LocalDateTime startTime = LocalDateTime.now();
       this.isActive = true;
       System.out.println("Exam session started at: " + startTime);
   }

   public void endSession() {
       this.isActive = false;
       studentKeyLoggers.forEach((studentId, logger) -> 
           System.out.println("Saving logs for student: " + studentId));
       System.out.println("Exam session ended at: " + LocalDateTime.now());
   }
   ```
    - Mengelola siklus hidup sesi ujian
    - Mencatat waktu mulai dan selesai
    - Menyimpan log aktivitas

2. **Manajemen Siswa**:
   ```java
   public void addActiveStudent(String studentId) {
       activeStudentIds.add(studentId);
       ExamKeyLogger keyLogger = new ExamKeyLogger();
       studentKeyLoggers.put(studentId, keyLogger);
       System.out.println("Student " + studentId + " added to session with keylogger");
   }
   ```
    - Menambahkan siswa ke sesi
    - Membuat logger khusus untuk siswa

3. **Akses Logger**:
   ```java
   public ExamKeyLogger getKeyLogger(String studentId) {
       return studentKeyLoggers.get(studentId);
   }
   ```
    - Mengambil logger spesifik siswa
    - Digunakan untuk mencatat aktivitas

## 3. Hubungan dengan Komponen Lain

### 1. Interaksi dengan ExamKeyLogger
```java
public List<KeyLogEntry> getStudentCheatingLogs(String studentId) {
    ExamKeyLogger logger = studentKeyLoggers.get(studentId);
    if (logger != null) {
        List<KeyLogEntry> logs = logger.getCheatingLogs();
        System.out.println("Retrieved " + logs.size() + " log entries for student " + studentId);
        return logs;
    }
    return new ArrayList<>();
}
```
- Mengakses log kecurangan siswa
- Menggunakan ExamKeyLogger untuk tracking

### 2. Interaksi dengan StudentDashboard
```java
// Di StudentDashboard.java
ExamSession session = new ExamSession();
session.addActiveStudent(student.getUserId());
session.startSession();
ExamKeyLogger keyLogger = session.getKeyLogger(student.getUserId());
```
- Menginisiasi sesi ujian
- Memonitor aktivitas siswa

### 3. Interaksi dengan KeyLogEntry
```java
// Melalui ExamKeyLogger
private void addLogEntry(String action) {
    KeyLogEntry entry = new KeyLogEntry(action);
    keyLogs.add(entry);
}
```
- Mencatat setiap aktivitas mencurigakan
- Membuat entri log baru

## 4. Best Practices Penggunaan

1. **Memulai Sesi Ujian**:
   ```java
   ExamSession session = new ExamSession();
   session.addActiveStudent("S001");
   session.startSession();
   ```

2. **Monitoring Aktivitas**:
   ```java
   ExamKeyLogger logger = session.getKeyLogger(studentId);
   if (session.isActive()) {
       logger.logKeyEvent(keyEvent);
   }
   ```

3. **Mengakhiri Sesi**:
   ```java
   List<KeyLogEntry> logs = session.getStudentCheatingLogs(studentId);
   session.endSession();
   ```

## 5. Tips Pengembangan

1. **Session State Management**:
   ```java
   private enum SessionState {
       PREPARING, ACTIVE, COMPLETED, CANCELLED
   }
   private SessionState state;
   ```

2. **Time Tracking**:
   ```java
   private LocalDateTime startTime;
   private LocalDateTime endTime;
   
   public Duration getSessionDuration() {
       return Duration.between(startTime, endTime);
   }
   ```

3. **Student Activity Summary**:
   ```java
   public Map<String, Integer> getStudentActivitySummary() {
       Map<String, Integer> summary = new HashMap<>();
       studentKeyLoggers.forEach((id, logger) -> 
           summary.put(id, logger.getCheatingLogs().size()));
       return summary;
   }
   ```

## 6. Keamanan dan Validasi

1. **Session Validation**:
   ```java
   private void validateSessionState() {
       if (!isActive) {
           throw new IllegalStateException("Session is not active");
       }
   }
   ```

2. **Student Validation**:
   ```java
   private void validateStudent(String studentId) {
       if (!activeStudentIds.contains(studentId)) {
           throw new IllegalArgumentException("Student not in session");
       }
   }
   ```

3. **Log Protection**:
   ```java
   public List<KeyLogEntry> getStudentCheatingLogs(String studentId) {
       validateStudent(studentId);
       ExamKeyLogger logger = studentKeyLoggers.get(studentId);
       return logger != null ? new ArrayList<>(logger.getCheatingLogs()) : 
                             new ArrayList<>();
   }
   ```

ExamSession.java adalah komponen kunci yang mengelola sesi ujian dan pemantauan aktivitas siswa. Class ini bertanggung jawab untuk:
1. Mengelola siklus hidup sesi ujian
2. Memantau dan mencatat aktivitas siswa
3. Menyediakan akses ke log kecurangan
4. Mengkoordinasikan komponen logging

Dengan struktur yang ada, ExamSession.java memberikan fondasi yang kuat untuk sistem monitoring ujian yang aman dan efektif.