-- ============================================================
-- EduLearn — Clear All User Data Script
-- Run this in MySQL as root
-- Password: Pankaj@123
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ── AUTH SERVICE (edulearn_auth) ─────────────────────────────
USE edulearn_auth;
TRUNCATE TABLE users;

-- ── COURSE SERVICE (edulearn_course) ────────────────────────
USE edulearn_course;
TRUNCATE TABLE courses;

-- ── LESSON SERVICE (edulearn_lesson) ────────────────────────
USE edulearn_lesson;
TRUNCATE TABLE lessons;
TRUNCATE TABLE resources;

-- ── ENROLLMENT SERVICE (edulearn_enrollment) ─────────────────
USE edulearn_enrollment;
TRUNCATE TABLE enrollments;

-- ── ASSESSMENT SERVICE (edulearn_assessment) ─────────────────
USE edulearn_assessment;
TRUNCATE TABLE attempt_answers;
TRUNCATE TABLE attempts;
TRUNCATE TABLE question_options;
TRUNCATE TABLE questions;
TRUNCATE TABLE quizzes;

-- ── PROGRESS SERVICE (edulearn_progress) ─────────────────────
USE edulearn_progress;
TRUNCATE TABLE progress;
TRUNCATE TABLE certificates;

-- ── PAYMENT SERVICE (edulearn_payment) ───────────────────────
USE edulearn_payment;
TRUNCATE TABLE payments;
TRUNCATE TABLE subscriptions;

-- ── DISCUSSION SERVICE (edulearn_discussion) ─────────────────
USE edulearn_discussion;
TRUNCATE TABLE replies;
TRUNCATE TABLE discussion_threads;

-- ── NOTIFICATION SERVICE (edulearn_notification) ─────────────
USE edulearn_notification;
TRUNCATE TABLE notifications;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'All EduLearn data cleared successfully.' AS Status;
