# EduLearn Platform — Demo Guide

> **Password for ALL seed accounts:** `password123`

---

## 🔐 Login Credentials

### Admin Account
| Field | Value |
|-------|-------|
| Email | `admin@edulearn.com` |
| Password | `password123` |
| Role | Admin |

### Instructor Accounts (5)

| # | Name | Email | Specialization |
|---|------|-------|----------------|
| 1 | Dr. Sarah Connor | `instructor@edulearn.com` | Full-Stack Development, Cloud Computing, Machine Learning |
| 2 | Alice Smith | `alice@edulearn.com` | UI/UX Design, React Native Mobile Development |
| 3 | Prof. Ravi Kumar | `ravi@edulearn.com` | Data Structures & Algorithms |
| 4 | *(Add manually)* Prof. Anita Sharma | `anita@edulearn.com` | Cybersecurity & Ethical Hacking |
| 5 | *(Add manually)* Dr. James Wilson | `james@edulearn.com` | DevOps & Automation |

### Student Accounts (5)

| # | Name | Email | Interest Area |
|---|------|-------|---------------|
| 1 | Ravi Pandey | `student@edulearn.com` | Full-Stack Development |
| 2 | *(Add manually)* Priya Mehta | `priya@edulearn.com` | Data Science & ML |
| 3 | *(Add manually)* Arjun Nair | `arjun@edulearn.com` | Mobile Development |
| 4 | *(Add manually)* Sneha Gupta | `sneha@edulearn.com` | Cloud Computing |
| 5 | *(Add manually)* Vikram Singh | `vikram@edulearn.com` | UI/UX Design |

---

## 📚 Courses to Add Manually in Front of Mentor (5)

> These courses should be added via the **Instructor Dashboard → Create Course** page during the live demo.

### Course 1: Cybersecurity Fundamentals
| Field | Value |
|-------|-------|
| Title | Cybersecurity Fundamentals |
| Description | Learn the basics of cybersecurity including network security, encryption, vulnerability assessment, and ethical hacking methodologies. |
| Category | Cybersecurity |
| Level | Beginner |
| Language | English |
| Price | ₹699 / $8.99 |
| Instructor | Prof. Anita Sharma |
| **Lesson 1** | Introduction to Cybersecurity Threats |
| **Lesson 2** | Network Security & Firewalls |
| **Lesson 3** | Encryption and Cryptography Basics |

### Course 2: DevOps with Docker & Jenkins
| Field | Value |
|-------|-------|
| Title | DevOps with Docker & Jenkins |
| Description | Master containerization with Docker, CI/CD pipelines with Jenkins, and infrastructure automation for modern software delivery. |
| Category | DevOps |
| Level | Intermediate |
| Language | English |
| Price | ₹999 / $12.99 |
| Instructor | Dr. James Wilson |
| **Lesson 1** | Docker Containers Deep Dive |
| **Lesson 2** | Jenkins Pipeline Configuration |
| **Lesson 3** | Infrastructure as Code with Terraform |

### Course 3: Python for Data Analysis
| Field | Value |
|-------|-------|
| Title | Python for Data Analysis |
| Description | Learn data wrangling, visualization, and statistical analysis using Pandas, NumPy, and Matplotlib for real-world datasets. |
| Category | Data Science |
| Level | Beginner |
| Language | English |
| Price | ₹599 / $7.99 |
| Instructor | Dr. Sarah Connor |
| **Lesson 1** | Setting up Jupyter Notebooks |
| **Lesson 2** | Data Wrangling with Pandas |
| **Lesson 3** | Data Visualization with Matplotlib |

### Course 4: Android App Development with Kotlin
| Field | Value |
|-------|-------|
| Title | Android App Development with Kotlin |
| Description | Build modern Android applications using Kotlin, Jetpack Compose, and Material Design 3 from scratch to deployment on Play Store. |
| Category | Mobile Development |
| Level | Intermediate |
| Language | Hindi |
| Price | ₹899 / $10.99 |
| Instructor | Alice Smith |
| **Lesson 1** | Kotlin Basics & Android Studio Setup |
| **Lesson 2** | Jetpack Compose UI Development |
| **Lesson 3** | API Integration & Room Database |

### Course 5: Agile & Scrum Project Management
| Field | Value |
|-------|-------|
| Title | Agile & Scrum Project Management |
| Description | Learn Agile methodology, Scrum framework, sprint planning, user stories, and how to manage software projects effectively using Jira. |
| Category | Project Management |
| Level | Beginner |
| Language | English |
| Price | Free (₹0) |
| Instructor | Prof. Ravi Kumar |
| **Lesson 1** | Agile Manifesto & Scrum Roles |
| **Lesson 2** | Sprint Planning & Daily Standups |
| **Lesson 3** | Retrospectives & Continuous Improvement |

---

## 📋 Pre-Seeded Courses (Already in Database)

| # | Title | Instructor | Category | Price | Quiz |
|---|-------|-----------|----------|-------|------|
| 1 | Mastering Full-Stack Development | Dr. Sarah Connor | Web Development | $99 | ✅ 5 questions |
| 2 | UI/UX Principles for Devs | Alice Smith | Design | $49 | ✅ 5 questions |
| 3 | Advanced Cloud Architecture | Dr. Sarah Connor | Cloud Computing | $149 | ✅ 5 questions |
| 4 | Data Structures in Java | Prof. Ravi Kumar | Computer Science | Free | ✅ 5 questions |
| 5 | Machine Learning Fundamentals | Dr. Sarah Connor | Data Science | $79 | ✅ 5 questions |
| 6 | React Native Mobile Apps | Alice Smith | Mobile Development | $89 | ✅ 5 questions |

---

## 🎯 Demo Flow for Mentor Presentation

### Step 1: Show Pre-Seeded Data
1. Login as **Admin** → Show 6 courses, 5 users, analytics
2. Login as **Student (Ravi Pandey)** → Browse courses, enroll, take quiz

### Step 2: Add a Course Live
1. Login as **Instructor (Dr. Sarah Connor)**
2. Navigate to **My Courses → Create New Course**
3. Fill in "Python for Data Analysis" details from the table above
4. Add 3 lessons
5. Submit for admin approval

### Step 3: Admin Approval
1. Login as **Admin**
2. Go to **Course Management** → Approve the submitted course
3. Course is now live for students

### Step 4: Student Enrollment & Payment
1. Login as **Student**
2. Browse → Find the newly approved course
3. Enroll (triggers real email notification via RabbitMQ)
4. Show email received in inbox

### Step 5: Admin Notification
1. Login as **Admin**
2. Go to **Platform Notifications**
3. Send an announcement to all users
4. Show real email delivery

---

## 🏗️ Architecture Overview

```
Frontend (React + Vite)  →  API Gateway (8080)  →  Microservices
                                                     ├── auth-service (8081)
                                                     ├── course-service (8082)
                                                     ├── enrollment-service (8083)
                                                     ├── payment-service (8084)
                                                     ├── lesson-service (8085)
                                                     ├── progress-service (8086)
                                                     ├── discussion-service (8087)
                                                     ├── assessment-service (8088)
                                                     └── notification-service (8089)

Infrastructure:
  ├── MySQL (3306)         — Persistent data
  ├── RabbitMQ (5672)      — Event-driven notifications
  └── Redis/Memurai (6379) — Caching layer
```

---

## 🚀 Startup Order

1. **Auto-start (Windows services):** MySQL, Redis (Memurai), RabbitMQ
2. **Start from IntelliJ:** auth → course → enrollment → payment → lesson → progress → discussion → assessment → notification → api-gateway
3. **Start frontend:** `cd edulearn-Frontend && npm run dev`
