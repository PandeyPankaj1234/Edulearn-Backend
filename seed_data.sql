-- ============================================================
-- EduLearn — Seed Data Script (schema-corrected)
-- Run with: Get-Content seed_data.sql | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pPankaj@123
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ── AUTH SERVICE ──────────────────────────────────────────────
USE edulearn_auth;

INSERT IGNORE INTO users
  (user_id, full_name, email, password_hash, role, mobile, bio, profile_pic_url, status, created_at)
VALUES
  (1, 'Admin User',       'admin@edulearn.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin',      NULL, NULL, NULL, 'Active', NOW()),
  (2, 'Dr. Sarah Connor', 'instructor@edulearn.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Instructor', NULL, 'Senior Software Engineer, 12 years experience.', NULL, 'Active', NOW()),
  (3, 'Ravi Pandey',      'student@edulearn.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Student',    NULL, NULL, NULL, 'Active', NOW()),
  (4, 'Alice Smith',      'alice@edulearn.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Instructor', NULL, 'UI/UX Designer and Frontend Educator.', NULL, 'Active', NOW()),
  (5, 'Prof. Ravi Kumar', 'ravi@edulearn.com',       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Instructor', NULL, NULL, NULL, 'Active', NOW());
-- password_hash above = bcrypt of "password123"

-- ── COURSE SERVICE ────────────────────────────────────────────
USE edulearn_course;

INSERT IGNORE INTO courses
  (course_id, title, description, category, level, language, price, instructor_id, thumbnail_url, total_duration, is_published, created_at)
VALUES
  (1, 'Mastering Full-Stack Development',
   'Learn to build scalable applications from scratch using React, Spring Boot, and Microservices.',
   'Web Development', 'Intermediate', 'English', 99.00, 2,
   'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=1200&q=80',
   1200, b'1', CURDATE()),

  (2, 'UI/UX Principles for Devs',
   'Design beautiful, accessible interfaces. Master Figma, CSS animations, and design systems.',
   'Design', 'Beginner', 'English', 49.00, 4,
   'https://images.unsplash.com/photo-1561070791-2526d30994b5?auto=format&fit=crop&w=600&q=80',
   450, b'1', CURDATE()),

  (3, 'Advanced Cloud Architecture',
   'Deploy, scale, and manage production infrastructure on AWS, GCP, and Kubernetes.',
   'Cloud Computing', 'Advanced', 'English', 149.00, 2,
   'https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80',
   1800, b'1', CURDATE()),

  (4, 'Data Structures in Java',
   'Ace coding interviews by mastering core algorithms and data structures.',
   'Computer Science', 'Intermediate', 'Hindi', 0.00, 5,
   'https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=600&q=80',
   900, b'1', CURDATE()),

  (5, 'Machine Learning Fundamentals',
   'From linear regression to neural networks — learn ML from scratch with Python.',
   'Data Science', 'Beginner', 'English', 79.00, 2,
   'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&w=600&q=80',
   1100, b'1', CURDATE()),

  (6, 'React Native Mobile Apps',
   'Build cross-platform iOS and Android apps with React Native and Expo.',
   'Mobile Development', 'Intermediate', 'English', 89.00, 4,
   'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?auto=format&fit=crop&w=600&q=80',
   1050, b'1', CURDATE());

-- ── LESSON SERVICE ────────────────────────────────────────────
USE edulearn_lesson;

INSERT IGNORE INTO lessons
  (lesson_id, course_id, title, content_url, duration_minutes, order_index, is_preview)
VALUES
  -- Course 1: Full-Stack Dev (4 lessons)
  (1,  1, 'Introduction to Microservices', 'https://www.w3schools.com/html/mov_bbb.mp4', 15, 1, b'1'),
  (2,  1, 'Setting up Spring Boot',        'https://www.w3schools.com/html/mov_bbb.mp4', 45, 2, b'0'),
  (3,  1, 'React Hooks Deep Dive',         'https://www.w3schools.com/html/mov_bbb.mp4', 60, 3, b'0'),
  (4,  1, 'Authentication with JWT',       'https://www.w3schools.com/html/mov_bbb.mp4', 55, 4, b'0'),
  -- Course 2: UI/UX (3 lessons)
  (5,  2, 'Design Thinking Fundamentals',  'https://www.w3schools.com/html/mov_bbb.mp4', 20, 1, b'1'),
  (6,  2, 'Figma for Developers',          'https://www.w3schools.com/html/mov_bbb.mp4', 40, 2, b'0'),
  (7,  2, 'CSS Animations & Transitions',  'https://www.w3schools.com/html/mov_bbb.mp4', 35, 3, b'0'),
  -- Course 3: Cloud (3 lessons)
  (8,  3, 'AWS Core Services Overview',    'https://www.w3schools.com/html/mov_bbb.mp4', 50, 1, b'1'),
  (9,  3, 'Kubernetes in Production',      'https://www.w3schools.com/html/mov_bbb.mp4', 70, 2, b'0'),
  (10, 3, 'CI/CD with GitHub Actions',     'https://www.w3schools.com/html/mov_bbb.mp4', 60, 3, b'0'),
  -- Course 4: Data Structures (3 lessons)
  (11, 4, 'Arrays & Linked Lists',         'https://www.w3schools.com/html/mov_bbb.mp4', 30, 1, b'1'),
  (12, 4, 'Trees & Graphs',                'https://www.w3schools.com/html/mov_bbb.mp4', 45, 2, b'0'),
  (13, 4, 'Dynamic Programming',           'https://www.w3schools.com/html/mov_bbb.mp4', 50, 3, b'0'),
  -- Course 5: ML (3 lessons)
  (14, 5, 'Linear Regression Explained',   'https://www.w3schools.com/html/mov_bbb.mp4', 25, 1, b'1'),
  (15, 5, 'Classification Algorithms',     'https://www.w3schools.com/html/mov_bbb.mp4', 40, 2, b'0'),
  (16, 5, 'Neural Networks from Scratch',  'https://www.w3schools.com/html/mov_bbb.mp4', 55, 3, b'0'),
  -- Course 6: React Native (3 lessons)
  (17, 6, 'Expo Setup & First App',        'https://www.w3schools.com/html/mov_bbb.mp4', 20, 1, b'1'),
  (18, 6, 'Navigation & State Management', 'https://www.w3schools.com/html/mov_bbb.mp4', 45, 2, b'0'),
  (19, 6, 'Publishing to App Stores',      'https://www.w3schools.com/html/mov_bbb.mp4', 35, 3, b'0');

-- ── ASSESSMENT (QUIZ) SERVICE ─────────────────────────────────
USE edulearn_assessment;

-- All 6 courses now have quizzes
INSERT IGNORE INTO quizzes
  (quiz_id, course_id, title, passing_score, time_limit_minutes, is_published, max_attempts)
VALUES
  (1, 1, 'Full-Stack Fundamentals Quiz',      70, 15, b'1', 3),
  (2, 2, 'UI/UX Principles Assessment',       70, 10, b'1', 3),
  (3, 3, 'Cloud Architecture Quiz',           75, 20, b'1', 3),
  (4, 4, 'Data Structures & Algorithms Quiz', 70, 20, b'1', 3),
  (5, 5, 'Machine Learning Basics Quiz',      70, 15, b'1', 3),
  (6, 6, 'React Native Development Quiz',     70, 15, b'1', 3);

-- ── QUIZ 1: Full-Stack Development (5 questions) ─────────────
INSERT IGNORE INTO questions
  (question_id, quiz_id, text, type, correct_answer, marks, order_index)
VALUES
  (1,  1, 'What hook is used to manage state in React functional components?',  'MCQ', 'useState',                        1, 1),
  (2,  1, 'Which annotation marks a Spring Boot entry point?',                  'MCQ', '@SpringBootApplication',           1, 2),
  (3,  1, 'What does REST stand for?',                                          'MCQ', 'Representational State Transfer',  1, 3),
  (4,  1, 'Which HTTP method is used to update an existing resource?',          'MCQ', 'PUT',                              1, 4),
  (5,  1, 'What is the default port for a Spring Boot application?',            'MCQ', '8080',                             1, 5);

INSERT IGNORE INTO question_options (question_id, option_value) VALUES
  (1, 'useEffect'), (1, 'useState'), (1, 'useRef'), (1, 'useMemo'),
  (2, '@Component'), (2, '@SpringBootApplication'), (2, '@RestController'), (2, '@Service'),
  (3, 'Remote Execution State Transfer'), (3, 'Representational State Transfer'),
  (3, 'Resource Entity State Transfer'),  (3, 'Remote Entity State Transfer'),
  (4, 'GET'), (4, 'POST'), (4, 'PUT'), (4, 'DELETE'),
  (5, '3000'), (5, '8080'), (5, '8443'), (5, '5000');

-- ── QUIZ 2: UI/UX Principles (5 questions) ───────────────────
INSERT IGNORE INTO questions
  (question_id, quiz_id, text, type, correct_answer, marks, order_index)
VALUES
  (6,  2, 'What does UX stand for?',                                    'MCQ', 'User Experience',     1, 1),
  (7,  2, 'Which tool is widely used for UI prototyping and design?',   'MCQ', 'Figma',               1, 2),
  (8,  2, 'What CSS property creates smooth transitions?',              'MCQ', 'transition',          1, 3),
  (9,  2, 'What is the minimum recommended touch target size (px)?',    'MCQ', '44px',                1, 4),
  (10, 2, 'Which color contrast ratio does WCAG AA require for text?',  'MCQ', '4.5:1',               1, 5);

INSERT IGNORE INTO question_options (question_id, option_value) VALUES
  (6,  'User Execution'), (6,  'User Experience'), (6,  'User Exchange'), (6,  'User Export'),
  (7,  'Adobe XD'), (7,  'Figma'), (7,  'Sketch'), (7,  'InVision'),
  (8,  'animation'), (8,  'transition'), (8,  'transform'), (8,  'keyframes'),
  (9,  '24px'), (9,  '32px'), (9,  '44px'), (9,  '56px'),
  (10, '2:1'), (10, '3:1'), (10, '4.5:1'), (10, '7:1');

-- ── QUIZ 3: Cloud Architecture (5 questions) ─────────────────
INSERT IGNORE INTO questions
  (question_id, quiz_id, text, type, correct_answer, marks, order_index)
VALUES
  (11, 3, 'What does S3 stand for in AWS?',                              'MCQ', 'Simple Storage Service',  1, 1),
  (12, 3, 'Which Kubernetes object manages a set of identical pods?',    'MCQ', 'Deployment',              1, 2),
  (13, 3, 'What is the purpose of a load balancer?',                     'MCQ', 'Distribute incoming traffic across servers', 1, 3),
  (14, 3, 'Which AWS service is used for serverless functions?',         'MCQ', 'Lambda',                  1, 4),
  (15, 3, 'What does CI/CD stand for?',                                  'MCQ', 'Continuous Integration / Continuous Deployment', 1, 5);

INSERT IGNORE INTO question_options (question_id, option_value) VALUES
  (11, 'Secure Socket Service'), (11, 'Simple Storage Service'), (11, 'Server Side Storage'), (11, 'Scalable Storage System'),
  (12, 'Service'), (12, 'Pod'), (12, 'Deployment'), (12, 'ConfigMap'),
  (13, 'Cache static files'), (13, 'Encrypt data at rest'), (13, 'Distribute incoming traffic across servers'), (13, 'Monitor application logs'),
  (14, 'EC2'), (14, 'Lambda'), (14, 'ECS'), (14, 'Fargate'),
  (15, 'Code Integration / Code Delivery'), (15, 'Continuous Integration / Continuous Deployment'),
  (15, 'Container Integration / Container Deployment'), (15, 'Cloud Integration / Cloud Delivery');

-- ── QUIZ 4: Data Structures in Java (5 questions) ────────────
INSERT IGNORE INTO questions
  (question_id, quiz_id, text, type, correct_answer, marks, order_index)
VALUES
  (16, 4, 'What is the time complexity of binary search?',               'MCQ', 'O(log n)',       1, 1),
  (17, 4, 'Which data structure uses FIFO ordering?',                    'MCQ', 'Queue',          1, 2),
  (18, 4, 'What is the worst-case time complexity of quicksort?',        'MCQ', 'O(n²)',          1, 3),
  (19, 4, 'Which traversal visits the root node first in a binary tree?','MCQ', 'Pre-order',      1, 4),
  (20, 4, 'What is the space complexity of a recursive fibonacci?',      'MCQ', 'O(n)',           1, 5);

INSERT IGNORE INTO question_options (question_id, option_value) VALUES
  (16, 'O(1)'), (16, 'O(n)'), (16, 'O(log n)'), (16, 'O(n log n)'),
  (17, 'Stack'), (17, 'Queue'), (17, 'Deque'), (17, 'Priority Queue'),
  (18, 'O(n)'), (18, 'O(n log n)'), (18, 'O(n²)'), (18, 'O(2^n)'),
  (19, 'In-order'), (19, 'Pre-order'), (19, 'Post-order'), (19, 'Level-order'),
  (20, 'O(1)'), (20, 'O(log n)'), (20, 'O(n)'), (20, 'O(n²)');

-- ── QUIZ 5: Machine Learning (5 questions) ───────────────────
INSERT IGNORE INTO questions
  (question_id, quiz_id, text, type, correct_answer, marks, order_index)
VALUES
  (21, 5, 'Which algorithm is used for binary classification?',          'MCQ', 'Logistic Regression', 1, 1),
  (22, 5, 'What does overfitting mean in ML?',                           'MCQ', 'Model performs well on training data but poorly on unseen data', 1, 2),
  (23, 5, 'Which Python library is primarily used for ML?',              'MCQ', 'scikit-learn',        1, 3),
  (24, 5, 'What is a neural network activation function?',               'MCQ', 'ReLU',                1, 4),
  (25, 5, 'What metric measures classification accuracy for imbalanced datasets?', 'MCQ', 'F1 Score', 1, 5);

INSERT IGNORE INTO question_options (question_id, option_value) VALUES
  (21, 'Linear Regression'), (21, 'Logistic Regression'), (21, 'K-Means'), (21, 'Random Forest'),
  (22, 'Model is too simple'), (22, 'Model performs well on training data but poorly on unseen data'),
  (22, 'Model is not trained'), (22, 'Model has too few parameters'),
  (23, 'NumPy'), (23, 'scikit-learn'), (23, 'Pandas'), (23, 'Matplotlib'),
  (24, 'Sigmoid'), (24, 'ReLU'), (24, 'Softmax'), (24, 'Tanh'),
  (25, 'Accuracy'), (25, 'Precision'), (25, 'F1 Score'), (25, 'Recall');

-- ── QUIZ 6: React Native (5 questions) ───────────────────────
INSERT IGNORE INTO questions
  (question_id, quiz_id, text, type, correct_answer, marks, order_index)
VALUES
  (26, 6, 'Which command creates a new Expo project?',                   'MCQ', 'npx create-expo-app', 1, 1),
  (27, 6, 'What is the React Native equivalent of HTML div?',            'MCQ', 'View',                1, 2),
  (28, 6, 'Which navigation library is most popular in React Native?',   'MCQ', 'React Navigation',    1, 3),
  (29, 6, 'How do you style components in React Native?',                'MCQ', 'StyleSheet.create()', 1, 4),
  (30, 6, 'What is the bridge in React Native?',                         'MCQ', 'Communication layer between JS and native code', 1, 5);

INSERT IGNORE INTO question_options (question_id, option_value) VALUES
  (26, 'npm init react-native'), (26, 'npx create-expo-app'), (26, 'expo init'), (26, 'npx react-native init'),
  (27, 'div'), (27, 'View'), (27, 'Container'), (27, 'Section'),
  (28, 'React Router'), (28, 'React Navigation'), (28, 'Next Router'), (28, 'Expo Router'),
  (29, 'CSS files'), (29, 'StyleSheet.create()'), (29, 'Tailwind CSS'), (29, 'Styled Components'),
  (30, 'A testing framework'), (30, 'A state management tool'),
  (30, 'Communication layer between JS and native code'), (30, 'A build system for iOS');

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'EduLearn seed data inserted successfully!' AS Status;
