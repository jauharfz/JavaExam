Saya akan menjelaskan tentang `User.java` secara fundamental dan perannya dalam sistem ini.

# Analisis User.java - Fundamental Guide

## 1. Class Overview
```java
public abstract class User {
    private String userId;
    private String username;
    private String password;
}
```

### Karakteristik Penting:
1. **Abstract Class**:
    - `User` adalah abstract class, yang berarti tidak bisa diinstansiasi langsung
    - Berfungsi sebagai "template" untuk tipe user spesifik (Student dan Lecturer)

2. **Atribut Dasar**:
   ```java
   private String userId;    // ID unik untuk setiap user
   private String username;  // Username untuk login
   private String password;  // Password untuk autentikasi
   ```

## 2. Constructor dan Methods

### Constructor
```java
public User(String userId, String username, String password) {
    this.userId = userId;
    this.username = username;
    this.password = password;
}
```
- Digunakan saat membuat instance Student atau Lecturer
- Menginisialisasi data dasar user

### Methods
1. **Getter Methods**:
   ```java
   public String getUserId() {
       return userId;
   }

   public String getUsername() {
       return username;
   }
   ```
    - Memberikan akses read-only ke userId dan username

2. **Password Validation**:
   ```java
   public boolean validatePassword(String inputPassword) {
       return this.password.equals(inputPassword);
   }
   ```
    - Memverifikasi password saat login
    - Digunakan dalam proses autentikasi

## 3. Hubungan dengan Komponen Lain

### Inheritance Hierarchy
```
User (abstract)
├── Student
└── Lecturer
```

1. **Student Class**:
    - Extends User
    - Menambah fungsi khusus untuk siswa (mengerjakan ujian, melihat nilai)

2. **Lecturer Class**:
    - Extends User
    - Menambah fungsi khusus untuk pengajar (membuat ujian, menilai)

### Penggunaan dalam Sistem

1. **Dalam ExamSystemUI**:
   ```java
   private static final Map<String, User> users = new HashMap<>();
   ```
    - Menyimpan semua user dalam sistem
    - Digunakan untuk autentikasi

2. **Dalam Login Process**:
   ```java
   User user = users.get(username);
   if (user != null && user.validatePassword(password)) {
       // Login berhasil
   }
   ```

## 4. Best Practices dalam Penggunaan

1. **Pembuatan User Baru**:
   ```java
   // Membuat student
   User student = new Student("S001", "student", "pass123");
   
   // Membuat lecturer
   User lecturer = new Lecturer("L001", "lecturer", "pass123");
   ```

2. **Keamanan**:
    - Password tidak disimpan dalam bentuk plain text (dalam sistem nyata)
    - Gunakan enkripsi atau hashing untuk password
    - Hindari mengekspos password melalui getter

3. **Validasi**:
    - Selalu validasi input sebelum membuat user baru
    - Pastikan userId dan username unik
    - Password memenuhi kriteria keamanan

turn hashPassword(inputPassword).