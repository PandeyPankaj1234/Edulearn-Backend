# 🎓 EduLearn Backend

A production-ready **microservices backend** for an online learning platform built with **Java 21**, **Spring Boot 3.2.5**, **MySQL**, **RabbitMQ**, and **Redis**.

---

## 🏗️ Architecture

```
Frontend (React + Vite)
        │
        ▼
API Gateway (:8080)          ← Single entry point, routes all requests
        │
        ├── auth-service        (:8081)  →  /api/auth/**
        ├── course-service      (:8082)  →  /api/courses/**
        ├── lesson-service      (:8083)  →  /api/lessons/**
        ├── enrollment-service  (:8084)  →  /api/enrollments/**
        ├── assessment-service  (:8085)  →  /api/quizzes/** & /api/attempts/**
        ├── payment-service     (:8086)  →  /api/payments/**
        ├── progress-service    (:8087)  →  /api/progress/**
        ├── discussion-service  (:8088)  →  /api/discussions/**
        └── notification-service(:8089)  →  /api/notifications/**

Infrastructure:
  ├── MySQL      (:3306)  — Separate DB per service
  ├── RabbitMQ   (:5672)  — Async event messaging
  └── Redis      (:6379)  — Caching (TTL: 10 min)
```

---

## 🚀 Services & Ports

| Service | Port | Database | Messaging |
|---|---|---|---|
| API Gateway | 8080 | — | — |
| Auth Service | 8081 | edulearn_auth | RabbitMQ, Redis |
| Course Service | 8082 | edulearn_course | RabbitMQ, Redis |
| Lesson Service | 8083 | edulearn_lesson | — |
| Enrollment Service | 8084 | edulearn_enrollment | RabbitMQ |
| Assessment Service | 8085 | edulearn_assessment | — |
| Payment Service | 8086 | edulearn_payment | RabbitMQ |
| Progress Service | 8087 | edulearn_progress | — |
| Discussion Service | 8088 | edulearn_discussion | — |
| Notification Service | 8089 | edulearn_notification | RabbitMQ + Gmail SMTP |

---

## 🛠️ Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.2.5
- **Gateway**: Spring Cloud Gateway MVC
- **Security**: Spring Security + JWT (jjwt 0.12.5) + Google OAuth2
- **Database**: MySQL 8 (one schema per service)
- **Caching**: Redis (via Spring Cache, TTL 10 min)
- **Messaging**: RabbitMQ (async event-driven notifications)
- **Email**: Gmail SMTP (Spring Mail)
- **Payments**: Razorpay
- **Build**: Maven 3.9 (multi-module)
- **Containerization**: Docker (auth-service Dockerfile included)

---

## 📁 Project Structure

```
edulearn-Backend/
├── pom.xml                        ← Parent POM (Java 21, Spring Boot 3.2.5)
├── api-gateway/                   ← Spring Cloud Gateway (port 8080)
├── auth-service/                  ← JWT auth + Google OAuth (port 8081)
│   └── Dockerfile
├── course-service/                ← Course CRUD + approval workflow (port 8082)
├── lesson-service/                ← Lessons & resources (port 8083)
├── enrollment-service/            ← Student enrollments (port 8084)
├── assessment-service/            ← Quizzes & attempts (port 8085)
├── payment-service/               ← Razorpay payments & subscriptions (port 8086)
├── progress-service/              ← Progress tracking & certificates (port 8087)
├── discussion-service/            ← Course discussion threads (port 8088)
├── notification-service/          ← Email alerts via RabbitMQ (port 8089)
├── seed_data.sql                  ← Pre-seeded demo data
├── clear_all_data.sql             ← Reset script
└── DEMO_README.md                 ← Demo credentials & flow guide
```

---

## ⚙️ Prerequisites

- Java 21+
- Maven 3.9+
- MySQL 8 (running on port 3306)
- Redis (running on port 6379)
- RabbitMQ (running on port 5672, default guest/guest)

---

## 🏁 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/PandeyPankaj1234/Edulearn-Backend.git
cd Edulearn-Backend
```

### 2. Create MySQL Databases

```sql
CREATE DATABASE edulearn_auth;
CREATE DATABASE edulearn_course;
CREATE DATABASE edulearn_lesson;
CREATE DATABASE edulearn_enrollment;
CREATE DATABASE edulearn_assessment;
CREATE DATABASE edulearn_payment;
CREATE DATABASE edulearn_progress;
CREATE DATABASE edulearn_discussion;
CREATE DATABASE edulearn_notification;
```

### 3. Seed Demo Data (Optional)

```bash
mysql -u root -p < seed_data.sql
```

### 4. Configure Environment Variables

Set these before running any service:

```env
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_jwt_secret_key
RABBITMQ_HOST=localhost
REDIS_HOST=localhost
RAZORPAY_KEY_ID=your_razorpay_key
RAZORPAY_KEY_SECRET=your_razorpay_secret
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

> ⚠️ Never commit real credentials to GitHub. Use environment variables or a `.env` file (already in `.gitignore`).

### 5. Build All Services

```bash
mvn clean install -DskipTests
```

### 6. Start Services in This Order

```
1. auth-service
2. course-service
3. enrollment-service
4. payment-service
5. lesson-service
6. progress-service
7. discussion-service
8. assessment-service
9. notification-service
10. api-gateway          ← Start last
```

```bash
cd auth-service && mvn spring-boot:run
```

---

## 📡 API Endpoints

### 🔐 Auth Service — `/api/auth` (port 8081)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, returns JWT |
| POST | `/api/auth/google` | Google OAuth2 login |
| GET | `/api/auth/profile?email=` | Get user profile |
| PUT | `/api/auth/profile/{userId}` | Update profile |
| PUT | `/api/auth/password/{userId}` | Change password |
| DELETE | `/api/auth/delete/{userId}` | Delete account |
| GET | `/api/auth/admin/users` | List all users (Admin) |
| GET | `/api/auth/admin/users/role/{role}` | Filter users by role |
| PUT | `/api/auth/admin/users/{userId}/suspend` | Suspend/unsuspend user |
| GET | `/api/auth/admin/users/search?name=` | Search users by name |

### 📚 Course Service — `/api/courses` (port 8082)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/courses` | Create course |
| GET | `/api/courses` | List all courses |
| GET | `/api/courses/{courseId}` | Get course by ID |
| GET | `/api/courses/category/{category}` | Filter by category |
| GET | `/api/courses/instructor/{instructorId}` | Filter by instructor |
| GET | `/api/courses/search?keyword=` | Search courses |
| GET | `/api/courses/featured` | Get featured courses |
| PUT | `/api/courses/{courseId}` | Update course |
| DELETE | `/api/courses/{courseId}` | Delete course |
| PUT | `/api/courses/{courseId}/submit-review` | Submit for admin review |
| PUT | `/api/courses/{courseId}/approve` | Approve course (Admin) |
| PUT | `/api/courses/{courseId}/reject?reason=` | Reject course (Admin) |
| GET | `/api/courses/admin/approval-status?status=` | Filter by approval status |

### 📖 Lesson Service — `/api/lessons` (port 8083)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/lessons` | Add lesson |
| GET | `/api/lessons/course/{courseId}` | Get lessons by course |
| GET | `/api/lessons/{lessonId}` | Get lesson by ID |
| PUT | `/api/lessons/{lessonId}` | Update lesson |
| DELETE | `/api/lessons/{lessonId}` | Delete lesson |
| GET | `/api/lessons/course/{courseId}/preview` | Get free preview lessons |
| GET | `/api/lessons/course/{courseId}/count` | Count lessons in course |
| POST | `/api/lessons/resources` | Add resource to lesson |
| GET | `/api/lessons/{lessonId}/resources` | Get lesson resources |
| DELETE | `/api/lessons/resources/{resourceId}` | Remove resource |

### 🎓 Enrollment Service — `/api/enrollments` (port 8084)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/enrollments` | Enroll in course |
| PUT | `/api/enrollments/{enrollmentId}/unenroll` | Unenroll |
| GET | `/api/enrollments/student/{studentId}` | Get student enrollments |
| GET | `/api/enrollments/course/{courseId}` | Get course enrollments |
| PUT | `/api/enrollments/{enrollmentId}/progress` | Update progress % |
| PUT | `/api/enrollments/{enrollmentId}/complete` | Mark as completed |
| GET | `/api/enrollments/check?studentId=&courseId=` | Check if enrolled |
| POST | `/api/enrollments/{enrollmentId}/certificate` | Issue certificate |
| GET | `/api/enrollments/course/{courseId}/count` | Enrollment count |
| GET | `/api/enrollments/all` | All enrollments (Admin) |

### 📝 Assessment Service — `/api/quizzes` & `/api/attempts` (port 8085)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/quizzes` | Create quiz |
| POST | `/api/quizzes/questions` | Add question to quiz |
| GET | `/api/quizzes/course/{courseId}` | Get quizzes by course |
| GET | `/api/quizzes/{quizId}/questions` | Get quiz questions |
| PUT | `/api/quizzes/{quizId}` | Update quiz |
| PUT | `/api/quizzes/{quizId}/publish` | Publish quiz |
| DELETE | `/api/quizzes/{quizId}` | Delete quiz |
| POST | `/api/attempts/start?quizId=&studentId=` | Start quiz attempt |
| POST | `/api/attempts/submit` | Submit attempt |
| GET | `/api/attempts/student/{studentId}` | Student's attempts |
| GET | `/api/attempts/quiz/{quizId}` | All attempts for quiz |
| GET | `/api/attempts/best-score?studentId=&quizId=` | Best score |
| GET | `/api/attempts/count?studentId=&quizId=` | Attempt count |

### 💳 Payment Service — `/api/payments` (port 8086)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments/process` | Process payment (Razorpay) |
| GET | `/api/payments/student/{studentId}` | Student payment history |
| GET | `/api/payments/course/{courseId}` | Course payments |
| PUT | `/api/payments/{paymentId}/refund` | Refund payment |
| GET | `/api/payments/revenue` | Total revenue (Admin) |
| GET | `/api/payments/all` | All payments (Admin) |
| POST | `/api/payments/subscribe` | Create subscription |
| PUT | `/api/payments/subscriptions/{id}/cancel` | Cancel subscription |
| PUT | `/api/payments/subscriptions/{id}/renew` | Renew subscription |
| PUT | `/api/payments/subscriptions/{id}/refund` | Refund subscription (Admin) |
| GET | `/api/payments/subscriptions/student/{studentId}` | Get subscription |
| GET | `/api/payments/subscriptions/active/{studentId}` | Check active status |
| GET | `/api/payments/subscriptions/all` | All subscriptions (Admin) |

### 📈 Progress Service — `/api/progress` (port 8087)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/progress/track` | Track lesson progress |
| PUT | `/api/progress/complete?studentId=&lessonId=` | Mark lesson complete |
| GET | `/api/progress/course?studentId=&courseId=&totalLessons=` | Course progress % |
| GET | `/api/progress/lesson?studentId=&lessonId=` | Lesson progress |
| GET | `/api/progress/student/{studentId}` | All student progress |
| POST | `/api/progress/certificates/issue` | Issue certificate |
| GET | `/api/progress/certificates?studentId=&courseId=` | Get certificate |
| GET | `/api/progress/certificates/verify/{code}` | Verify certificate by code |
| GET | `/api/progress/certificates/student/{studentId}` | Student certificates |
| GET | `/api/progress/certificates/all` | All certificates (Admin) |

### 💬 Discussion Service — `/api/discussions` (port 8088)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/discussions/threads` | Create discussion thread |
| GET | `/api/discussions/threads/course/{courseId}` | Threads by course |
| GET | `/api/discussions/threads/lesson/{lessonId}` | Threads by lesson |
| GET | `/api/discussions/threads/author/{authorId}` | Threads by author |
| GET | `/api/discussions/threads/search?keyword=` | Search threads |
| PUT | `/api/discussions/threads/{threadId}/pin` | Pin thread |
| PUT | `/api/discussions/threads/{threadId}/close` | Close thread |
| DELETE | `/api/discussions/threads/{threadId}` | Delete thread |
| POST | `/api/discussions/replies` | Post reply |
| GET | `/api/discussions/replies/thread/{threadId}` | Get replies |
| GET | `/api/discussions/replies/thread/{threadId}/count` | Reply count |
| PUT | `/api/discussions/replies/{replyId}/upvote` | Upvote reply |
| PUT | `/api/discussions/replies/{replyId}/accept` | Accept as best answer |
| DELETE | `/api/discussions/replies/{replyId}` | Delete reply |

### 🔔 Notification Service — `/api/notifications` (port 8089)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/notifications/send` | Send notification |
| POST | `/api/notifications/send-bulk` | Broadcast to multiple users |
| POST | `/api/notifications/email?toEmail=&subject=&body=` | Send direct email |
| GET | `/api/notifications/user/{userId}` | User notifications |
| GET | `/api/notifications/user/{userId}/unread` | Unread notifications |
| GET | `/api/notifications/user/{userId}/count` | Unread count |
| PUT | `/api/notifications/{notificationId}/read` | Mark as read |
| PUT | `/api/notifications/user/{userId}/read-all` | Mark all as read |
| DELETE | `/api/notifications/{notificationId}` | Delete notification |
| GET | `/api/notifications/all` | All notifications (Admin) |

---

## 🌿 Branch Strategy

```
main              ← Stable, production-ready
  └── dev         ← Integration branch
        ├── feature/auth-service
        ├── feature/course-service
        ├── feature/lesson-service
        ├── feature/enrollment-service
        ├── feature/assessment-service
        ├── feature/payment-service
        ├── feature/progress-service
        ├── feature/discussion-service
        ├── feature/notification-service
        └── feature/api-gateway
```

---

## 🐳 Docker (Auth Service)

```bash
cd auth-service
docker build -t edulearn-auth-service .
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/edulearn_auth \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  edulearn-auth-service
```

---

## 🧪 Running Tests

```bash
# All services
mvn test

# Single service
cd auth-service && mvn test
```

---

## 👥 Demo Accounts

| Role | Email | Password |
|---|---|---|
| Admin | admin@edulearn.com | password123 |
| Instructor | instructor@edulearn.com | password123 |
| Student | student@edulearn.com | password123 |

> See `DEMO_README.md` for the full demo flow, all seed accounts, and pre-loaded course data.

---

## 👤 Author

**Pankaj Pandey**
- GitHub: [@PandeyPankaj1234](https://github.com/PandeyPankaj1234)

---

> Built with ❤️ using Spring Boot 3.2.5 Microservices | Java 21
