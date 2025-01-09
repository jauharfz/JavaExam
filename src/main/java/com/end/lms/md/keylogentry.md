# Analisis KeyLogEntry.java - Fundamental Guide

## 1. Class Overview
```java
public class KeyLogEntry {
    private String action;
    private LocalDateTime timestamp;
}
```

### Karakteristik Penting:
1. **Atribut Utama**:
    - `action`: Menyimpan deskripsi aktivitas yang dicatat
    - `timestamp`: Waktu kejadian aktivitas

## 2. Constructor dan Methods

### Constructor
```java
public KeyLogEntry(String action) {
    this.action = action;
    this.timestamp = LocalDateTime.now();
}
```
- Membuat entri log baru
- Otomatis mencatat waktu saat pembuatan
- Menyimpan deskripsi aktivitas

### Core Method
```java
@Override
public String toString() {
    return String.format("[%s] %s", timestamp, action);
}
```
- Format output log: `[waktu] aktivitas`
- Digunakan untuk menampilkan log

## 3. Integrasi dengan Komponen Lain

### 1. Interaksi dengan ExamKeyLogger
```java
// Di ExamKeyLogger.java
private void addLogEntry(String action) {
    long currentTime = System.currentTimeMillis();
    if (!action.equals(lastAction) || (currentTime - lastLogTime) >= LOG_DELAY) {
        KeyLogEntry entry = new KeyLogEntry(action);  // Membuat log baru
        keyLogs.add(entry);
        lastAction = action;
        lastLogTime = currentTime;
    }
}
```
- ExamKeyLogger membuat instance KeyLogEntry
- Menyimpan dalam daftar logs

### 2. Interaksi dengan LecturerDashboard
```java
// Di LecturerDashboard.java
private void loadActivityLogs(Exam exam, ListView<String> logsList) {
    logsList.getItems().clear();
    examLogs.forEach((key, logs) -> {
        if (key.contains(exam.getExamId())) {
            String[] parts = key.split("_");
            String studentId = parts[0];
            logsList.getItems().add("Student: " + studentId);
            logs.forEach(log -> logsList.getItems().add("  " + log));  // Menggunakan toString()
            logsList.getItems().add("");
        }
    });
}
```
- Menampilkan log aktivitas di dashboard dosen
- Menggunakan toString() untuk format tampilan

## 4. Contoh Format Log

### 1. Format Dasar
```java
[2025-01-02T17:03:45] Pressed Ctrl+C
```

### 2. Tipe-tipe Log
```java
[2025-01-02T17:03:45] Pressed Alt+Tab
[2025-01-02T17:03:46] Switching Window
[2025-01-02T17:03:47] Pressed Ctrl+V
```

## 5. Best Practices Penggunaan

1. **Pembuatan Log Entry**:
   ```java
   KeyLogEntry entry = new KeyLogEntry("Pressed Ctrl+C");
   System.out.println(entry); // Output: [2025-01-02T17:03:45] Pressed Ctrl+C
   ```

2. **Dalam List**:
   ```java
   List<KeyLogEntry> logs = new ArrayList<>();
   logs.add(new KeyLogEntry("Window Unfocused"));
   logs.forEach(System.out::println);
   ```

## 6. Keamanan dan Validasi

1. **Immutability**:
   ```java
   // Atribut final untuk mencegah modifikasi
   private final String action;
   private final LocalDateTime timestamp;
   ```

2. **Validasi Input**:
   ```java
   public KeyLogEntry(String action) {
       if (action == null || action.trim().isEmpty()) {
           throw new IllegalArgumentException("Action cannot be null or empty");
       }
       this.action = action;
       this.timestamp = LocalDateTime.now();
   }
   ```

## 7. Rekomendasi Pengembangan

1. **Tambahan Getter Methods**:
   ```java
   public String getAction() {
       return action;
   }

   public LocalDateTime getTimestamp() {
       return timestamp;
   }
   ```

2. **Format Timestamp Kustom**:
   ```java
   public String getFormattedTimestamp() {
       return timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME);
   }
   ```

3. **Kategori Log**:
   ```java
   private LogCategory category;
   
   public enum LogCategory {
       KEYBOARD_EVENT,
       WINDOW_EVENT,
       SYSTEM_EVENT
   }
   ```

KeyLogEntry.java adalah komponen sederhana namun penting yang:
1. Menyimpan informasi aktivitas mencurigakan
2. Mencatat waktu kejadian
3. Menyediakan format standar untuk tampilan log
4. Mendukung sistem monitoring ujian

Dengan struktur yang ada, KeyLogEntry.java memberikan format yang konsisten untuk pencatatan dan pelaporan aktivitas dalam sistem ujian online.