Baik, saya akan menjelaskan narasi untuk bagian UI (ExamSystemUI.java, LecturerDashboard.java, dan StudentDashboard.java) secara bertahap:

# 1. ExamSystemUI.java - Halaman Login dan Inisialisasi

## Alur Utama:
1. Aplikasi dimulai dengan menampilkan halaman login
2. Sistem memuat data awal (dummy data untuk testing)
3. User dapat login sebagai student atau lecturer

## Method-method Kunci:
- `start(Stage primaryStage)`:
    - Entry point aplikasi
    - Memulai dengan inisialisasi data
    - Menampilkan layar login

- `showLoginScreen(Stage stage)`:
    - Membuat tampilan login dengan username dan password
    - Menampilkan form dalam layout yang rapi
    - Mengatur styling untuk UI yang menarik

- `handleLogin(Stage stage, String username, String password, Label statusLabel)`:
    - Memverifikasi kredensial user
    - Mengarahkan ke dashboard yang sesuai (student/lecturer)
    - Menampilkan pesan error jika login gagal

- `initializeData()`:
    - Membuat data dummy untuk testing
    - Menginisialisasi user (students dan lecturers)
    - Membuat contoh ujian dengan soal-soal

# 2. LecturerDashboard.java - Dashboard Dosen

## Fitur Utama:
1. Manajemen Ujian (Exam Management)
2. Monitoring Aktivitas (Activity Logs)
3. Penilaian (Grading)

## Tab Manajemen Ujian:
- `createExamManagementTab()`:
    - Menampilkan daftar ujian yang ada
    - Opsi untuk melihat hasil ujian
    - Opsi untuk mengedit nilai

- `showExamResults(Exam exam)`:
    - Menampilkan hasil ujian per mahasiswa
    - Format tampilan dalam bentuk tabel
    - Opsi untuk kembali ke dashboard

- `showScoreEditor(Exam exam)`:
    - Interface untuk mengedit nilai
    - Fitur auto-grading
    - Penyimpanan perubahan nilai

# 3. StudentDashboard.java - Dashboard Mahasiswa

## Fitur Utama:
1. Daftar Ujian Tersedia
2. Pengerjaan Ujian
3. Melihat Nilai

## Method-method Kunci:
- `createAvailableExamsTab()`:
    - Menampilkan ujian yang dapat diikuti
    - Filter ujian berdasarkan eligibilitas
    - Tombol untuk memulai ujian

- `showExamInterface(Exam exam)`:
    - Interface pengerjaan ujian
    - Monitoring aktivitas mahasiswa
    - Sistem anti-kecurangan

- `submitExam()`:
    - Proses pengumpulan jawaban
    - Pencatatan log aktivitas
    - Kembali ke dashboard setelah selesai

Ini adalah gambaran awal dari struktur UI sistem. Apakah Anda ingin saya menjelaskan bagian tertentu lebih detail?