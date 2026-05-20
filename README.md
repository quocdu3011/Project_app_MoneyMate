# MoneyMate 💰

Ứng dụng quản lý tài chính cá nhân trên Android, giúp theo dõi thu chi, lập ngân sách và phân tích tài chính thông qua biểu đồ trực quan. Toàn bộ dữ liệu lưu cục bộ, không cần internet.

---

## Tính năng

- **Giao dịch** — Thêm, sửa, xóa thu/chi/chuyển khoản
- **Tài khoản/Ví** — Quản lý nhiều ví (tiền mặt, ngân hàng, ví điện tử)
- **Danh mục** — 19 danh mục mặc định, hỗ trợ tùy chỉnh
- **Ngân sách** — Đặt hạn mức chi tiêu theo tháng, cảnh báo khi vượt 80%/100%
- **Báo cáo** — Biểu đồ tròn và cột theo tháng/năm
- **Giao dịch định kỳ** — Tự động tạo giao dịch lặp lại (ngày/tuần/tháng/năm)
- **Bảo mật** — Khóa ứng dụng bằng PIN hoặc vân tay/Face ID
- **Giao diện** — Material Design 3, hỗ trợ Dark/Light mode

---

## Công nghệ

| Thành phần | Công nghệ |
|------------|-----------|
| Ngôn ngữ | Java (JDK 17) |
| Build config | Kotlin DSL (`build.gradle.kts`) |
| UI | XML Layouts + View Binding + Material 3 |
| Kiến trúc | MVVM + Clean Architecture |
| Database | Room (SQLite) + RxJava 3 |
| DI | Hilt (Dagger) |
| Navigation | Navigation Component |
| Biểu đồ | MPAndroidChart |
| Background | WorkManager |
| Min SDK | API 26 (Android 8.0) |

---

## Kiến trúc

```
app/
└── java/com/example/moneymate/
    ├── data/               # Tầng dữ liệu
    │   ├── local/          # Room DB, DAOs, Entities, SharedPrefs
    │   ├── mapper/         # Entity ↔ Domain mapping
    │   └── repository/     # Repository implementations
    ├── di/                 # Hilt modules
    ├── domain/             # Tầng nghiệp vụ
    │   ├── model/          # POJO models + enums
    │   └── repository/     # Repository interfaces
    ├── presentation/       # Tầng giao diện
    │   ├── adapter/        # RecyclerView adapters
    │   └── screen/         # Fragments + ViewModels
    ├── util/               # Tiện ích
    └── worker/             # WorkManager workers
```

---

## Cài đặt & Chạy

### Yêu cầu
- Android Studio Hedgehog (2023.1.1) trở lên
- JDK 17
- Android SDK API 26+

### Các bước

```bash
# 1. Clone hoặc mở project trong Android Studio
# 2. Sync Gradle
File → Sync Project with Gradle Files

# 3. Build
./gradlew assembleDebug

# 4. Chạy trên thiết bị/emulator
./gradlew installDebug
```

---

## Cơ sở dữ liệu

| Bảng | Mô tả |
|------|-------|
| `accounts` | Tài khoản/ví của người dùng |
| `categories` | Danh mục thu/chi |
| `transactions` | Giao dịch thu/chi/chuyển khoản |
| `budgets` | Ngân sách theo tháng |
| `recurring_transactions` | Giao dịch định kỳ |

---

## Màn hình chính

| Màn hình | Mô tả |
|----------|-------|
| Home | Tổng quan số dư, giao dịch gần đây |
| Giao dịch | Danh sách, tìm kiếm, lọc |
| Thêm giao dịch | Form nhập thu/chi/chuyển khoản |
| Ngân sách | Hạn mức chi tiêu theo danh mục |
| Báo cáo | Biểu đồ tròn, cột theo tháng |
| Tài khoản | Quản lý ví/tài khoản |
| Danh mục | Quản lý danh mục |
| Cài đặt | Theme, bảo mật, tiền tệ |

---

## Tài liệu

- [Thiết kế hệ thống](THIET_KE_HE_THONG.md) — Kiến trúc, ERD, wireframes, tech stack đầy đủ

---

## Giấy phép

Dự án học tập — Bài tập lớn môn Lập trình Android.
