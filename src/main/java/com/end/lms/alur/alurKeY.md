Baik, saya akan menjelaskan sistem monitoring dan logging untuk mencegah kecurangan dalam ujian:

# 1. ExamKeyLogger.java - Pencatat Aktivitas Keyboard

## Tujuan:
- Memantau aktivitas keyboard mahasiswa selama ujian
- Mendeteksi upaya kecurangan seperti copy-paste atau switch window

## Method-method Utama:
- `logKeyEvent(KeyEvent event)`:
    - Mencatat kombinasi tombol yang ditekan
    - Fokus pada tombol-tombol mencurigakan:
        - Ctrl+C (Copy)
        - Ctrl+V (Paste)
        - Alt+Tab (Switch window)
        - Windows key
        - PrintScreen

- `logWindowUnfocused()`:
    - Mencatat ketika mahasiswa berpindah window
    - Indikasi kemungkinan membuka aplikasi/browser lain

- `addLogEntry(String action)`:
    - Menambah log dengan timestamp
    - Mencegah duplikasi log dalam waktu berdekatan (1 detik)

# 2. ExamSession.java - Manajemen Sesi Ujian

## Tujuan:
- Mengatur sesi ujian aktif
- Mengelola multiple mahasiswa dan logger mereka
- Menyimpan log aktivitas per mahasiswa

## Method-method Utama:
- `startSession()`:
    - Memulai sesi ujian
    - Mencatat waktu mulai
    - Mengaktifkan monitoring

- `endSession()`:
    - Mengakhiri sesi ujian
    - Menyimpan semua log aktivitas
    - Mencatat waktu selesai

- `addActiveStudent(String studentId)`:
    - Mendaftarkan mahasiswa ke sesi
    - Membuat keylogger khusus untuk mahasiswa
    - Memulai pemantauan aktivitas

- `getStudentCheatingLogs(String studentId)`:
    - Mengambil log aktivitas mencurigakan
    - Digunakan untuk review dosen

# 3. KeyLogEntry.java - Format Log Aktivitas

## Tujuan:
- Menyimpan detail aktivitas mencurigakan
- Format standar untuk logging

## Komponen:
- `action`: Jenis aktivitas yang tercatat
- `timestamp`: Waktu kejadian
- Format output: `[waktu] jenis_aktivitas`

## Contoh Log:
```
[2025-01-09 14:49:30] Pressed Ctrl+C
[2025-01-09 14:49:35] Pressed Ctrl+V
[2025-01-09 14:50:01] Switching Window
```

# Alur Kerja Sistem Anti-Kecurangan:

1. Saat ujian dimulai:
    - ExamSession dibuat
    - Setiap mahasiswa didaftarkan
    - KeyLogger diaktifkan

2. Selama ujian:
    - Sistem memantau aktivitas keyboard
    - Mencatat aktivitas mencurigakan
    - Menyimpan timestamp setiap kejadian

3. Setelah ujian:
    - Session diakhiri
    - Log disimpan
    - Dosen dapat mereview aktivitas mencurigakan

Sistem ini membantu menjaga integritas ujian dengan:
- Deteksi copy-paste
- Monitoring perpindahan window
- Pencatatan screenshot
- Tracking penggunaan tombol Windows