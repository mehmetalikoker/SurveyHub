# SurveyHub

Anket oluşturma ve yönetme için Spring Boot tabanlı bir admin web uygulaması.

## Teknoloji Yığını

- **Java 24**
- **Spring Boot 3.5.16** (Web, Thymeleaf, Data JPA, Validation, Security)
- **H2** (dosya tabanlı) — geliştirme veritabanı
- **Maven Wrapper** (`mvnw` / `mvnw.cmd`) — ayrıca Maven kurmaya gerek yok

> Prodüksiyon hedefi **Oracle** (bankada devreye alınacak). Şu an sadece H2 ile geliştirme yapılıyor; Oracle'a geçiş planlanıyor ama henüz uygulanmadı.

## Gereksinimler

- JDK 24 kurulu olmalı ve `JAVA_HOME` ona işaret etmeli (sistemde farklı bir Java sürümü varsayılan olabilir, dikkat).

## Çalıştırma

```bash
./mvnw.cmd spring-boot:run
```

Uygulama `http://localhost:8080` üzerinden ayağa kalkar ve `/admin` ekranına yönlendirir.

## Yapılandırma

Ayarlar `src/main/resources/application.yml` içinde; aşağıdaki ortam değişkenleriyle override edilebilir:

| Değişken | Açıklama | Varsayılan |
|---|---|---|
| `SURVEYHUB_API_KEY` | Mobil/harici kanal API anahtarı | `dev-key` |
| `SURVEYHUB_ADMIN_USERNAME` | Admin kullanıcı adı | `admin` |
| `SURVEYHUB_ADMIN_PASSWORD` | Admin parolası | `changeme` |

**Prod'a çıkmadan önce** `SURVEYHUB_ADMIN_PASSWORD` mutlaka ayarlanmalı — varsayılan placeholder aktifken uygulama log'da uyarı basar (bkz. `AdminUserSeeder`).

## Veritabanı

- Geliştirmede H2, dosya olarak `./data/surveyhub` altında tutulur (`.gitignore`'da hariç).
- H2 konsolu: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/surveyhub`, kullanıcı: `sa`, şifre: boş).

## Proje Yapısı

```
domain/       Survey, Question, QuestionOption, SurveyStatus, QuestionType
repository/   Spring Data JPA repository'leri
service/      SurveyService
web/          Admin Thymeleaf controller'ları, form login
security/     AdminUser, AdminUserDetailsService, AdminUserSeeder
config/       SecurityConfig
```

## Kimlik Doğrulama

Admin arayüzü Spring Security form login ile korunur (`/login` hariç tüm sayfalar `ROLE_ADMIN` ister). Açılışta tek bir admin hesabı otomatik seed edilir.
