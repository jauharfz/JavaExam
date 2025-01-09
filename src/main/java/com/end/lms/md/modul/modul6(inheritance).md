Mari saya analisis implementasi konsep Inheritance dalam project ini secara detail:

## 1. Hierarki Inheritance

### Base Class (User)
```java
public abstract class User {
    private String userId;
    private String username;
    private String password;

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    // Base methods
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
```

### Derived Classes

1. Student Class (extends User):
```java
public class Student extends User {
    private Map<String, List<Answer>> examAnswers = new HashMap<>();

    public Student(String userId, String username, String password) {
        super(userId, username, password); // Memanggil constructor parent
    }

    // Additional method khusus Student
    public void submitAnswer(Answer answer) {
        if (answer.validate()) {
            answer.submit();
            examAnswers.computeIfAbsent(answer.getExamId(), k -> new ArrayList<>())
                    .add(answer);
        }
    }
}
```

2. Lecturer Class (extends User):
```java
public class Lecturer extends User {
    public Lecturer(String userId, String username, String password) {
        super(userId, username, password); // Memanggil constructor parent
    }
}
```

## 2. Penggunaan Inheritance dalam Sistem

### 1. Polymorphic References
Di kelas `ExamSystemUI`:
```java
private void handleLogin(Stage stage, String username, String password, Label statusLabel) {
    User user = users.get(username);
    if (user != null && user.validatePassword(password)) {
        if (user instanceof Student) {
            showStudentDashboard(stage, (Student) user);
        } else {
            showLecturerDashboard(stage, (Lecturer) user);
        }
    }
}
```

### 2. Inheritance Features yang Digunakan

1. **Method Inheritance**
    - Semua derived classes (Student dan Lecturer) mewarisi methods dari User:
        - `getUserId()`
        - `getUsername()`
        - `validatePassword()`

2. **Constructor Chaining**
    - Penggunaan `super()` untuk memanggil constructor parent class:
   ```java
   public Student(String userId, String username, String password) {
       super(userId, username, password);
   }
   ```

3. **Access to Parent Members**
    - Child classes dapat mengakses protected/public members dari parent class
    - Contoh: `student.getUsername()` mengakses method yang diwarisi dari User

## 3. Manfaat Implementasi Inheritance

1. **Code Reusability**
    - Logic authentication dan user management ditempatkan di base class User
    - Tidak perlu menduplikasi kode untuk validasi password di setiap user type

2. **Polymorphic Behavior**
    - Sistem dapat memperlakukan Student dan Lecturer sebagai User
    - Memungkinkan pengelolaan user yang fleksibel dalam `Map<String, User> users`

3. **Type Safety**
    - Inheritance memastikan bahwa semua tipe user memiliki properti dan method dasar yang diperlukan
    - Memudahkan type checking dengan `instanceof`

4. **Extensibility**
    - Mudah menambahkan tipe user baru dengan extend User
    - Tidak perlu mengubah logic authentication yang sudah ada

## 4. Object-Oriented Design Patterns

1. **Template Method Pattern**
    - Base class User menyediakan struktur dasar
    - Child classes dapat meng-override atau menambah fungsionalitas

2. **Liskov Substitution Principle**
    - Student dan Lecturer dapat digunakan di manapun User diharapkan
    - Contoh dalam method handleLogin yang menerima User sebagai parameter

## 5. Saran Pengembangan

1. Menambahkan abstract methods di User untuk memaksa implementasi di child classes
2. Implementasi method toString() untuk representasi string yang lebih baik
3. Menambahkan validasi tambahan yang spesifik untuk masing-masing tipe user
4. Menggunakan interface untuk mendefinisikan kontrak behavior

Project ini sudah mengimplementasikan inheritance dengan baik untuk membuat hierarki user yang extensible dan maintainable. Penggunaan inheritance memungkinkan sharing of common functionality sambil tetap mempertahankan spesifikasi masing-masing tipe user.