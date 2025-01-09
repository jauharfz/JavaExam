# Analisis ExamKeyLogger.java - Fundamental Guide

## 1. Class Overview
```java
public class ExamKeyLogger {
    private final List<KeyLogEntry> keyLogs;
    private String lastAction;
    private long lastLogTime;
    private static final long LOG_DELAY = 1000; // 1 detik delay
}
```

### Karakteristik Penting:
1. **Atribut Utama**:
    - `keyLogs`: Menyimpan daftar aktivitas mencurigakan
    - `lastAction`: Menyimpan aksi terakhir yang dicatat
    - `lastLogTime`: Waktu log terakhir
    - `LOG_DELAY`: Delay antar log (1000ms)

## 2. Constructor dan Methods Utama

### Constructor
```java
public ExamKeyLogger() {
    this.keyLogs = new ArrayList<>();
    this.lastAction = "";
    this.lastLogTime = 0;
}
```
- Inisialisasi logger baru
- Menyiapkan list kosong untuk logs
- Reset tracking waktu

### Core Methods

1. **Log Key Events**:
   ```java
   public void logKeyEvent(KeyEvent event) {
       String action = createKeyEventDescription(event);
       if (action != null) {
           addLogEntry(action);
       }
   }
   ```
    - Mencatat event keyboard
    - Filter event yang mencurigakan

2. **Window Focus Tracking**:
   ```java
   public void logWindowUnfocused() {
       addLogEntry("Switching Window");
   }
   ```
    - Mencatat ketika window tidak fokus
    - Indikasi potensial cheating

3. **Log Entry Management**:
   ```java
   private void addLogEntry(String action) {
       long currentTime = System.currentTimeMillis();
       if (!action.equals(lastAction) || (currentTime - lastLogTime) >= LOG_DELAY) {
           KeyLogEntry entry = new KeyLogEntry(action);
           keyLogs.add(entry);
           lastAction = action;
           lastLogTime = currentTime;
       }
   }
   ```
    - Menambah log baru
    - Mencegah duplikasi log
    - Implementasi delay

## 3. Event Detection System

### 1. Key Combination Detection
```java
private String createKeyEventDescription(KeyEvent event) {
    // Control + Key combinations
    if (event.isControlDown()) {
        if (event.getCode() == KeyCode.C) return "Pressed Ctrl+C";
        if (event.getCode() == KeyCode.V) return "Pressed Ctrl+V";
        if (event.getCode() == KeyCode.TAB) return "Pressed Ctrl+Tab";
        if (event.getCode() == KeyCode.WINDOWS) return "Pressed Ctrl+Win";
    }

    // Alt + Key combinations
    if (event.isAltDown()) {
        if (event.getCode() == KeyCode.TAB) return "Pressed Alt+Tab";
        if (event.getCode() == KeyCode.F4) return "Pressed Alt+F4";
    }

    // Single key events
    if (event.getCode() == KeyCode.WINDOWS) return "Pressed Win";
    if (event.getCode() == KeyCode.PRINTSCREEN) return "Pressed PrtSc";

    return null;
}
```
- Deteksi kombinasi tombol mencurigakan
- Fokus pada shortcut umum untuk cheating
- Return null untuk key yang tidak dicurigai

## 4. Integrasi dengan Komponen Lain

### 1. Interaksi dengan ExamSession
```java
// Di ExamSession.java
public ExamKeyLogger getKeyLogger(String studentId) {
    return studentKeyLoggers.get(studentId);
}
```
- ExamSession mengelola logger untuk setiap siswa
- Akses ke log melalui studentId

### 2. Interaksi dengan StudentDashboard
```java
// Di StudentDashboard.java
examScene.addEventFilter(KeyEvent.ANY, event -> {
    if (session.isActive()) {
        keyLogger.logKeyEvent(event);
    }
});
```
- Mencatat semua key events saat ujian
- Filter berdasarkan status sesi

### 3. Interaksi dengan KeyLogEntry
```java
public List<KeyLogEntry> getCheatingLogs() {
    return new ArrayList<>(keyLogs);
}
```
- Menyediakan akses ke log aktivitas
- Return copy untuk keamanan data

## 5. Best Practices Penggunaan

1. **Inisialisasi Logger**:
   ```java
   ExamKeyLogger logger = new ExamKeyLogger();
   ```

2. **Logging Events**:
   ```java
   // Key event logging
   logger.logKeyEvent(keyEvent);

   // Window focus logging
   logger.logWindowUnfocused();
   ```

3. **Accessing Logs**:
   ```java
   List<KeyLogEntry> cheatingLogs = logger.getCheatingLogs();
   cheatingLogs.forEach(log -> System.out.println(log));
   ```

## 6. Keamanan dan Validasi

1. **Event Filtering**:
   ```java
   private boolean isValidEvent(KeyEvent event) {
       return event != null && event.getCode() != null;
   }
   ```

2. **Log Protection**:
   ```java
   public List<KeyLogEntry> getCheatingLogs() {
       return new ArrayList<>(keyLogs); // Return copy
   }
   ```

3. **Delay Implementation**:
   ```java
   private boolean shouldLog(String action, long currentTime) {
       return !action.equals(lastAction) || 
              (currentTime - lastLogTime) >= LOG_DELAY;
   }
   ```

ExamKeyLogger.java adalah komponen penting yang:
1. Mendeteksi aktivitas mencurigakan
2. Mencatat kombinasi tombol tertentu
3. Memantau fokus window
4. Menyediakan log untuk review

Dengan struktur yang ada, ExamKeyLogger.java memberikan sistem monitoring yang efektif untuk mencegah kecurangan dalam ujian online.