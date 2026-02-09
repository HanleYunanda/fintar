# Project Documentation - FINTAR

==================================================
📌 INFORMASI PROJECT
==================================================
- **Nama Project**: Fintar
- **Deskripsi Singkat**: Project Bootcamp ITDP Fintar Apps - Platform manajemen pinjaman (Loan Management System).
- **Target Platform**: Backend API (Support Web & Mobile Clients)
- **Jenis Aplikasi**: Enterprise / Financial Services
- **Tujuan Utama**: Menyediakan layanan backend terintegrasi untuk pengelolaan nasabah, pengajuan pinjaman, dan manajemen produk finansial dengan keamanan berbasis role.

==================================================
📄 DOKUMENTASI LENGKAP
==================================================

# 1. Overview Project
### Latar Belakang
Fintar dikembangkan untuk memodernisasi proses pengajuan dan pengelolaan pinjaman. Sistem ini menggantikan proses manual dengan solusi digital yang terukur.

### Masalah yang Diselesaikan
- Lambatnya proses pengajuan dan persetujuan pinjaman.
- Kesulitan tracking status aplikasi pinjaman.
- Manajemen data nasabah yang terfragmentasi.

### Solusi
Backend API yang kuat (Robust) menggunakan Spring Boot yang menangani seluruh siklus hidup pinjaman dari registrasi user, upload dokumen, scoring/plafon, hingga pencairan dana.

### Target User
- **Nasabah**: Pengguna yang mengajukan pinjaman.
- **Admin/Staff**: Pengguna internal yang memverifikasi dan menyetujui pinjaman.

# 2. Scope & Limitasi
### In Scope
- Autentikasi & Otorisasi (Login Biasa & Google OAuth).
- Manajemen Pengguna & Role (RBAC).
- Manajemen Data Nasabah & Dokumen.
- Siklus Hidup Pinjaman (Apply, Approve, Reject, Disburse).
- Manajemen Produk & Plafon (Credit Limit).
- Laporan & Dashboard.

### Out of Scope
- Frontend UI (Dokumentasi ini fokus pada Backend API).
- Payment Gateway Integration (Simulasi internal saja).

### Asumsi Sistem
- Database SQL Server sudah tersedia dan terkonfigurasi.
- Redis tersedia untuk caching.
- SMTP Server (via Mailtrap) aktif untuk notifikasi email.

# 3. Arsitektur Sistem
### High-Level Architecture
Sistem menggunakan **Layered Architecture** standar Spring Boot:

1.  **Presentation Layer (Controller)**: Menangani HTTP Request/Response.
2.  **Business Logic Layer (Service)**: Menangani logic bisnis validasi dan alur kerja.
3.  **Data Access Layer (Repository)**: Berinteraksi dengan database (SQL Server/Redis).
4.  **Database Layer**: SQL Server sebagai primary storage.

### Diagram Alur Data
`Client (Web/Mobile) -> Security Filter -> Controller -> Service -> Repository -> Database`

### Pola Arsitektur
- **MVC (Model-View-Controller)**: Diimplementasikan via Spring Web MVC (fokus pada REST API).
- **DTO Pattern**: Menggunakan Data Transfer Object untuk memisahkan entity database dari contract API.

# 4. Tech Stack

| Kategori | Teknologi | Alasan Penggunaan |
| :--- | :--- | :--- |
| **Framework** | Spring Boot 4.0.1 | Framework Java modern, robust, dan 'opinionated' untuk pengembangan cepat. |
| **Bahasa** | Java 21 | Versi LTS terbaru dengan fitur modern (Records, Pattern Matching). |
| **Database** | SQL Server (MSSQL) | Standar enterprise, relasional kuat. |
| **Cache** | Redis | Caching performa tinggi untuk data yang sering diakses. |
| **Auth** | Spring Security + JWT | Stateless authentication standard industry. |
| **Social Auth** | Firebase Admin SDK | Integrasi mudah untuk Google Login. |
| **CORS/Web** | Spring WebMVC | Setup REST API standar. |
| **Docs** | SpringDoc (OpenAPI 3) | Auto-generate dokumentasi API (Swagger UI). |
| **Build Tool** | Maven | Dependency management standar Java. |
| **Container** | Docker | Deployment yang konsisten di berbagai environment. |

# 5. Struktur Project
Struktur folder mengikuti standar Maven layout:

- `src/main/java/com/example/fintar`
    - `config`: Konfigurasi aplikasi (Security, Redis, OpenAPI, Firebase).
    - `controller`: Endpoint handler (REST API).
    - `dto`: Data Transfer Objects (Request/Response schemas).
    - `entity`: JPA Entities mapping ke tabel database.
    - `repository`: Interface Spring Data JPA.
    - `service`: Business logic implementation.
    - `scheduler`: Background tasks.
    - `aspect`: Cross-cutting concerns (Logging).
    - `base`: Base classes for responses/entities.

- `src/main/resources`
    - `application.properties`: Konfigurasi env (DB credentials, JWT secret).
    - `files`: Default upload directory.

# 6. Fitur Utama

### A. Authentication & User Management
- **Deskripsi**: Handle registrasi, login, dan manajemen role.
- **Components**: `AuthController`, `PermissionController`, `RoleController`.
- **Flow**: User Register -> Verify -> Login -> Dapat JWT Token.

### B. Customer Profiling
- **Deskripsi**: Pengelolaan data diri nasabah dan dokumen pendukung.
- **Components**: `CustomerDetailController`, `DocumentController`.
- **Dependencies**: Perlu user yang sudah login.

### C. Loan Management
- **Deskripsi**: Inti bisnis - pengajuan dan approval pinjaman.
- **Components**: `LoanController`.
- **Flow**: User Apply -> Status 'Pending' -> Admin Review -> Approve/Reject -> Disburse.

### D. Product & Plafond
- **Deskripsi**: Master data untuk jenis produk pinjaman dan limit kredit user.
- **Components**: `ProductController`, `PlafondController`.

# 7. Alur Bisnis (Business Flow)

### Flow Utama: Pengajuan Pinjaman
1.  **Registrasi**: User baru mendaftar (via Email atau Google).
2.  **Lengkapi Data**: User mengisi Customer Detail & Upload Dokumen (KTP, NPWP).
3.  **Cek Plafon**: Sistem (atau Admin) menentukan limit (Plafond) user.
4.  **Pengajuan**: User memilih Produk dan mengajukan Pinjaman sesuai limit.
5.  **Review**: Admin melihat list pinjaman masuk.
6.  **Keputusan**: Admin melakukan Approval atau Rejection.
7.  **Pencairan**: Jika approve, masuk proses Disbursement.

### Flow Alternatif
- **Retry Login**: Jika password salah 3x (tergantung config security).
- **Insufficient Plafond**: User tidak bisa mengajukan melebihi limit.

# 8. Data & API

### Struktur Data Utama (Entity)
- `User`: Akun login.
- `CustomerDetail`: Data profil (KTP, Alamat).
- `Loan`: Transaksi pinjaman.
- `Product`: Master produk.
- `Plafond`: Limit kredit user.

### API & Validasi
- **Standard Response**: Menggunakan wrapper `ApiResponse<T>` (Status, Message, Data).
- **Validasi**: Menggunakan Bean Validation (`@Valid`, `@NotNull`) di DTO.

# 9. State Management
- **Stateless Backend**: API tidak menyimpan session user di memory server.
- **JWT (JSON Web Token)**: State autentikasi dibawa oleh client di header `Authorization: Bearer <token>`.

# 10. Security
- **Authentication**: JWT Based. Token validasi dilakukan di `JwtAuthenticationFilter`.
- **Authorization**: Annotation based (`@PreAuthorize("hasRole('ADMIN')")`).
- **Data Protection**: Password di-hash menggunakan BCrypt.
- **CORS**: Dikonfigurasi untuk allow origin `http://localhost:4200` (Frontend).

# 11. Background Process
- **Scheduler**: `CustomerPlafondScheduler`.
- **Fungsi**: Kemungkinan digunakan untuk reset limit, kalkulasi bunga, atau update status otomatis secara periodik.

# 12. Logging & Monitoring
- **Logging**: Menggunakan SLF4J + Logback.
- **Config**: Level DEBUG diset untuk SQL dan Transaction di `application.properties`.
- **Implementation**: Menggunakan `LoggingAspect` untuk trace entry/exit method (AOP).

# 13. Testing Strategy
- **Unit Test**: JUnit 5 + Mockito (Support library ada di pom).
- **Integration Test**: Spring Boot Test (`@SpringBootTest`).

# 14. Deployment
- **Method**: Docker Containerization.
- **Build**: `mvn clean package`.
- **CI/CD**: GitHub Actions workflows tersedia di `.github/workflows`.

# 15. Known Issues & Technical Debt
- **TODOs**: Terdapat marker TODO di `ReportService.java` (perlu dicek implementasi report).
- **Hardcoded Secrets**: Pastikan secrets di `application.properties` (seperti JWT Secret, DB Password) diganti Environment Variable saat masuk Production.

# 16. Future Improvement
- **Refactoring**: Sentralisasi handling file upload.
- **Feature**: Notifikasi Real-time (WebSocket) untuk update status pinjaman.
- **Security**: Implementasi Refresh Token rotation.

# 17. Cara Menjalankan Project (Getting Started)

### Requirement
- JDK 21
- Maven
- SQL Server (Running di port 1433)
- Redis (Running di port 6379)

### Setup & Run
1.  **Clone Repo**: `git clone ...`
2.  **Config DB**: Update username/password SQL Server di `application.properties`.
3.  **Build**: `mvn clean install`
4.  **Run**: `mvn spring-boot:run`
5.  **Access Swagger**: Buka `http://localhost:8080/swagger-ui.html`

### Troubleshooting
- **Connection Refused**: Pastikan SQL Server dan Redis menyala.
- **Port Conflict**: Cek apakah port 8080 sudah digunakan aplikasi lain.

# 18. Kontak & Kontributor
- **Role**: Backend Team.
- **Kontribusi**: Gunakan Pull Request mechanism ke branch `develop`. Jaga coverage test tetap tinggi.

