-- ============================================================
--  Student Information System - Complete Database Setup
--  MySQL Database: sisdb
--  Run this in phpMyAdmin or MySQL CLI
-- ============================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

-- ============================================================
-- CREATE DATABASE
-- ============================================================
CREATE DATABASE IF NOT EXISTS `sisdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `sisdb`;

-- ============================================================
-- TABLE: users (Login credentials for all roles)
-- ============================================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL UNIQUE,
  `password` VARCHAR(100) NOT NULL,
  `role` ENUM('ADMIN','TEACHER','STUDENT') NOT NULL,
  `fullName` VARCHAR(150) DEFAULT NULL,
  `email` VARCHAR(150) DEFAULT NULL,
  `active` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: techer_db (Teacher information)
-- ============================================================
DROP TABLE IF EXISTS `techer_db`;
CREATE TABLE `techer_db` (
  `teacher_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `email_id` VARCHAR(150) UNIQUE,
  `qualification` VARCHAR(100) DEFAULT 'M.Tech',
  `phone_number` VARCHAR(20) DEFAULT NULL,
  `salary` DECIMAL(10,2) DEFAULT 50000.00,
  `dept_name` VARCHAR(100) DEFAULT 'Computer Science',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: student (Student information)
-- ============================================================
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `roll_no` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `age` INT(11) DEFAULT 18,
  `username` VARCHAR(100) UNIQUE,
  `password` VARCHAR(100) DEFAULT NULL,
  `address` TEXT,
  `caste` VARCHAR(50) DEFAULT 'General',
  `gender` ENUM('Male','Female','Other') DEFAULT 'Male',
  `class` VARCHAR(50) DEFAULT 'FY',
  `religion` VARCHAR(50) DEFAULT 'Hindu',
  `email` VARCHAR(150) DEFAULT NULL,
  `phone_number` VARCHAR(20) DEFAULT NULL,
  `enrollmentYear` INT(11) DEFAULT 2024,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`roll_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: course (Course information)
-- ============================================================
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `courseCode` VARCHAR(20) NOT NULL,
  `course_name` VARCHAR(200) NOT NULL,
  `teacherId` INT(11) DEFAULT NULL,
  `description` TEXT,
  `credits` VARCHAR(10) DEFAULT '3',
  `active` TINYINT(1) DEFAULT 1,
  `capacity` INT(11) DEFAULT 30,
  `enrolledCount` INT(11) DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`courseCode`),
  FOREIGN KEY (`teacherId`) REFERENCES `techer_db`(`teacher_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: grades (Student grades)
-- ============================================================
DROP TABLE IF EXISTS `grades`;
CREATE TABLE `grades` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `roll_no` INT(11) NOT NULL,
  `courseCode` VARCHAR(20) NOT NULL,
  `marks` DECIMAL(5,2) NOT NULL,
  `semester` VARCHAR(20) DEFAULT 'SEM-1',
  `academicYear` INT(11) DEFAULT 2024,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`roll_no`) REFERENCES `student`(`roll_no`) ON DELETE CASCADE,
  FOREIGN KEY (`courseCode`) REFERENCES `course`(`courseCode`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: enroll (Student course enrollments)
-- ============================================================
DROP TABLE IF EXISTS `enroll`;
CREATE TABLE `enroll` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `roll_no` INT(11) NOT NULL,
  `courseCode` VARCHAR(20) NOT NULL,
  `enrollment_date` DATE DEFAULT (CURRENT_DATE),
  `status` ENUM('ENROLLED','DROPPED','COMPLETED') DEFAULT 'ENROLLED',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_enrollment` (`roll_no`, `courseCode`),
  FOREIGN KEY (`roll_no`) REFERENCES `student`(`roll_no`) ON DELETE CASCADE,
  FOREIGN KEY (`courseCode`) REFERENCES `course`(`courseCode`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: attendance (Student attendance records)
-- ============================================================
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `roll_no` INT(11) NOT NULL,
  `courseCode` VARCHAR(20) NOT NULL,
  `date` DATE NOT NULL,
  `status` ENUM('PRESENT','ABSENT','LATE') DEFAULT 'PRESENT',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_attendance` (`roll_no`, `courseCode`, `date`),
  FOREIGN KEY (`roll_no`) REFERENCES `student`(`roll_no`) ON DELETE CASCADE,
  FOREIGN KEY (`courseCode`) REFERENCES `course`(`courseCode`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: schedule (Course schedules)
-- ============================================================
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `courseCode` VARCHAR(20) NOT NULL,
  `day_of_week` VARCHAR(20) NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `room` VARCHAR(20) DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`courseCode`) REFERENCES `course`(`courseCode`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- INSERT SAMPLE DATA: USERS
-- ============================================================
INSERT INTO `users` (`username`, `password`, `role`, `fullName`, `email`, `active`) VALUES
('admin', 'admin123', 'ADMIN', 'System Administrator', 'admin@sis.edu', 1),
('mrsmith', 'teach123', 'TEACHER', 'John Smith', 'mrsmith@sis.edu', 1),
('msjones', 'teach456', 'TEACHER', 'Mary Jones', 'msjones@sis.edu', 1),
('jdoe', 'pass123', 'TEACHER', 'Jane Doe', 'jdoe@sis.edu', 1),
('alice', 'alice123', 'STUDENT', 'Alice Johnson', 'alice@sis.edu', 1),
('bob', 'bob123', 'STUDENT', 'Bob Williams', 'bob@sis.edu', 1),
('yash', 'pass123', 'STUDENT', 'Yash Patel', 'yash@sis.edu', 1),
('charlie', 'charlie123', 'STUDENT', 'Charlie Brown', 'charlie@sis.edu', 1),
('diana', 'diana123', 'STUDENT', 'Diana Prince', 'diana@sis.edu', 1),
('eve', 'eve123', 'STUDENT', 'Eve Wilson', 'eve@sis.edu', 1);

-- ============================================================
-- INSERT SAMPLE DATA: TEACHERS
-- ============================================================
INSERT INTO `techer_db` (`name`, `email_id`, `qualification`, `phone_number`, `salary`, `dept_name`) VALUES
('John Smith', 'mrsmith', 'Ph.D', '9876543210', 85000.00, 'Computer Science'),
('Mary Jones', 'msjones', 'M.Tech', '9876543211', 75000.00, 'Computer Science'),
('Jane Doe', 'jdoe', 'Ph.D', '9876543212', 90000.00, 'Information Technology'),
('Robert Brown', 'rbrown', 'M.Sc', '9876543213', 65000.00, 'Mathematics'),
('Sarah Davis', 'sdavis', 'M.Tech', '9876543214', 80000.00, 'Physics'),
('Michael Wilson', 'mwilson', 'Ph.D', '9876543215', 95000.00, 'Electronics');

-- ============================================================
-- INSERT SAMPLE DATA: STUDENTS
-- ============================================================
INSERT INTO `student` (`name`, `age`, `username`, `password`, `address`, `caste`, `gender`, `class`, `religion`, `email`, `phone_number`, `enrollmentYear`) VALUES
('Alice Johnson', 20, 'alice', 'alice123', '123 Main St, Mumbai', 'General', 'Female', 'SY', 'Christian', 'alice@sis.edu', '9876543001', 2023),
('Bob Williams', 21, 'bob', 'bob123', '456 Oak Ave, Delhi', 'OBC', 'Male', 'TY', 'Hindu', 'bob@sis.edu', '9876543002', 2022),
('Yash Patel', 19, 'yash', 'pass123', '789 Pine Rd, Ahmedabad', 'General', 'Male', 'SY', 'Hindu', 'yash@sis.edu', '9876543003', 2023),
('Charlie Brown', 22, 'charlie', 'charlie123', '321 Elm St, Pune', 'SC', 'Male', 'TY', 'Buddhist', 'charlie@sis.edu', '9876543004', 2022),
('Diana Prince', 20, 'diana', 'diana123', '654 Maple Dr, Bangalore', 'General', 'Female', 'SY', 'Christian', 'diana@sis.edu', '9876543005', 2023),
('Eve Wilson', 21, 'eve', 'eve123', '987 Cedar Ln, Chennai', 'OBC', 'Female', 'TY', 'Hindu', 'eve@sis.edu', '9876543006', 2022),
('Frank Miller', 19, 'frank', 'frank123', '147 Birch Rd, Hyderabad', 'General', 'Male', 'FY', 'Hindu', 'frank@sis.edu', '9876543007', 2024),
('Grace Lee', 20, 'grace', 'grace123', '258 Walnut Ave, Kolkata', 'General', 'Female', 'SY', 'Buddhist', 'grace@sis.edu', '9876543008', 2023),
('Henry Taylor', 22, 'henry', 'henry123', '369 Cherry St, Lucknow', 'ST', 'Male', 'TY', 'Christian', 'henry@sis.edu', '9876543009', 2022),
('Ivy Martinez', 19, 'ivy', 'ivy123', '741 Spruce Dr, Jaipur', 'OBC', 'Female', 'FY', 'Hindu', 'ivy@sis.edu', '9876543010', 2024);

-- ============================================================
-- INSERT SAMPLE DATA: COURSES
-- ============================================================
INSERT INTO `course` (`courseCode`, `course_name`, `teacherId`, `description`, `credits`, `active`, `capacity`, `enrolledCount`) VALUES
('CS101', 'Introduction to Programming', 1, 'Fundamentals of C programming and problem solving', '4', 1, 40, 0),
('CS201', 'Data Structures', 1, 'Arrays, Linked Lists, Trees, and Graphs', '4', 1, 35, 0),
('CS301', 'Database Systems', 2, 'SQL, Normalization, and Database Design', '3', 1, 30, 0),
('CS401', 'Web Development', 2, 'HTML, CSS, JavaScript, and React', '3', 1, 30, 0),
('IT101', 'Information Technology Basics', 3, 'Introduction to IT and computing', '3', 1, 40, 0),
('IT201', 'Networking', 3, 'Computer Networks and Protocols', '4', 1, 35, 0),
('MATH101', 'Engineering Mathematics I', 4, 'Calculus and Linear Algebra', '4', 1, 50, 0),
('MATH201', 'Engineering Mathematics II', 4, 'Differential Equations and Complex Analysis', '4', 1, 45, 0),
('PHY101', 'Physics for Engineers', 5, 'Mechanics, Thermodynamics, and Waves', '3', 1, 40, 0),
('PHY201', 'Electronics', 5, 'Circuit Analysis and Electronic Devices', '4', 1, 35, 0);

-- ============================================================
-- INSERT SAMPLE DATA: ENROLLMENTS
-- ============================================================
INSERT INTO `enroll` (`roll_no`, `courseCode`, `enrollment_date`, `status`) VALUES
(1, 'CS101', '2024-01-15', 'ENROLLED'),
(1, 'CS201', '2024-01-15', 'ENROLLED'),
(1, 'MATH101', '2024-01-15', 'ENROLLED'),
(2, 'CS101', '2023-01-10', 'COMPLETED'),
(2, 'CS201', '2023-01-10', 'COMPLETED'),
(2, 'CS301', '2023-07-15', 'ENROLLED'),
(3, 'CS101', '2024-01-15', 'ENROLLED'),
(3, 'CS401', '2024-01-15', 'ENROLLED'),
(3, 'IT101', '2024-01-15', 'ENROLLED'),
(4, 'CS301', '2023-01-10', 'COMPLETED'),
(4, 'CS401', '2023-07-15', 'ENROLLED'),
(5, 'CS101', '2024-01-15', 'ENROLLED'),
(5, 'IT201', '2024-01-15', 'ENROLLED'),
(5, 'PHY101', '2024-01-15', 'ENROLLED'),
(6, 'CS201', '2023-01-10', 'COMPLETED'),
(6, 'CS301', '2023-07-15', 'ENROLLED'),
(6, 'MATH201', '2023-07-15', 'ENROLLED');

-- Update enrolled counts
UPDATE course SET enrolledCount = (SELECT COUNT(*) FROM enroll WHERE enroll.courseCode = course.courseCode AND enroll.status = 'ENROLLED');

-- ============================================================
-- INSERT SAMPLE DATA: GRADES
-- ============================================================
INSERT INTO `grades` (`roll_no`, `courseCode`, `marks`, `semester`, `academicYear`) VALUES
(2, 'CS101', 85.50, 'SEM-1', 2023),
(2, 'CS201', 78.00, 'SEM-1', 2023),
(4, 'CS301', 92.00, 'SEM-1', 2023),
(6, 'CS201', 88.50, 'SEM-1', 2023),
(1, 'CS101', 75.00, 'SEM-1', 2024),
(3, 'CS101', 82.50, 'SEM-1', 2024),
(5, 'CS101', 90.00, 'SEM-1', 2024);

-- ============================================================
-- INSERT SAMPLE DATA: ATTENDANCE
-- ============================================================
INSERT INTO `attendance` (`roll_no`, `courseCode`, `date`, `status`) VALUES
(1, 'CS101', '2024-03-01', 'PRESENT'),
(1, 'CS101', '2024-03-02', 'PRESENT'),
(1, 'CS101', '2024-03-03', 'ABSENT'),
(1, 'CS101', '2024-03-04', 'PRESENT'),
(1, 'CS101', '2024-03-05', 'LATE'),
(2, 'CS301', '2024-03-01', 'PRESENT'),
(2, 'CS301', '2024-03-02', 'PRESENT'),
(2, 'CS301', '2024-03-03', 'PRESENT'),
(2, 'CS301', '2024-03-04', 'ABSENT'),
(2, 'CS301', '2024-03-05', 'PRESENT'),
(3, 'CS101', '2024-03-01', 'PRESENT'),
(3, 'CS101', '2024-03-02', 'LATE'),
(3, 'CS101', '2024-03-03', 'PRESENT'),
(3, 'CS101', '2024-03-04', 'PRESENT'),
(3, 'CS101', '2024-03-05', 'PRESENT'),
(5, 'CS101', '2024-03-01', 'PRESENT'),
(5, 'CS101', '2024-03-02', 'PRESENT'),
(5, 'CS101', '2024-03-03', 'PRESENT'),
(5, 'CS101', '2024-03-04', 'PRESENT'),
(5, 'CS101', '2024-03-05', 'ABSENT');

-- ============================================================
-- INSERT SAMPLE DATA: SCHEDULE
-- ============================================================
INSERT INTO `schedule` (`courseCode`, `day_of_week`, `start_time`, `end_time`, `room`) VALUES
('CS101', 'Monday', '09:00:00', '10:30:00', 'LAB-101'),
('CS101', 'Wednesday', '09:00:00', '10:30:00', 'LAB-101'),
('CS101', 'Friday', '11:00:00', '12:30:00', 'LAB-101'),
('CS201', 'Tuesday', '14:00:00', '15:30:00', 'ROOM-201'),
('CS201', 'Thursday', '14:00:00', '15:30:00', 'ROOM-201'),
('CS301', 'Monday', '11:00:00', '12:30:00', 'ROOM-301'),
('CS301', 'Wednesday', '11:00:00', '12:30:00', 'ROOM-301'),
('CS401', 'Tuesday', '09:00:00', '10:30:00', 'LAB-102'),
('CS401', 'Thursday', '09:00:00', '10:30:00', 'LAB-102'),
('IT101', 'Monday', '14:00:00', '15:30:00', 'ROOM-101'),
('IT101', 'Wednesday', '14:00:00', '15:30:00', 'ROOM-101'),
('IT201', 'Tuesday', '11:00:00', '12:30:00', 'ROOM-202'),
('IT201', 'Thursday', '11:00:00', '12:30:00', 'ROOM-202'),
('MATH101', 'Monday', '08:00:00', '09:00:00', 'ROOM-101'),
('MATH101', 'Wednesday', '08:00:00', '09:00:00', 'ROOM-101'),
('MATH101', 'Friday', '08:00:00', '09:00:00', 'ROOM-101'),
('MATH201', 'Tuesday', '08:00:00', '09:30:00', 'ROOM-202'),
('MATH201', 'Thursday', '08:00:00', '09:30:00', 'ROOM-202'),
('PHY101', 'Wednesday', '14:00:00', '15:30:00', 'LAB-301'),
('PHY101', 'Friday', '14:00:00', '15:30:00', 'LAB-301');

-- ============================================================
-- DISPLAY SUMMARY
-- ============================================================
SELECT 'Database setup completed successfully!' AS Status;
SELECT 'Tables: users, techer_db, student, course, grades, enroll, attendance, schedule' AS Tables;
