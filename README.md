# Student Information System (SIS)

A console-based Student Information System built with **Core Java 17** and **Maven**.

## Features

| Feature              | Admin | Teacher | Student |
|----------------------|-------|---------|---------|
| Manage Students      | ✅    | —       | —       |
| Manage Teachers      | ✅    | —       | —       |
| Manage Courses       | ✅    | —       | —       |
| Manage Schedules     | ✅    | —       | —       |
| View All Records     | ✅    | —       | —       |
| View Assigned Courses| —     | ✅      | —       |
| Assign Grades        | —     | ✅      | —       |
| Mark Attendance      | —     | ✅      | —       |
| Enroll / Withdraw    | —     | —       | ✅      |
| View My Grades & GPA | —     | —       | ✅      |
| View My Schedule     | —     | —       | ✅      |
| View My Attendance   | —     | —       | ✅      |
| Change Password      | ✅    | ✅      | ✅      |

## Architecture

```
com.sis
├── Main.java                  ← entry point + demo data seeder
├── model/                     ← domain entities
│   ├── User.java (abstract)
│   ├── Admin.java
│   ├── Teacher.java
│   ├── Student.java
│   ├── Course.java
│   ├── Grade.java
│   ├── Enrollment.java
│   ├── Schedule.java
│   ├── Attendance.java
│   └── Role.java
├── repository/                ← in-memory data stores
│   ├── Repository.java (interface)
│   ├── InMemoryRepository.java (abstract)
│   ├── UserRepository.java
│   ├── StudentRepository.java
│   ├── TeacherRepository.java
│   ├── CourseRepository.java
│   ├── GradeRepository.java
│   ├── EnrollmentRepository.java
│   ├── ScheduleRepository.java
│   └── AttendanceRepository.java
├── service/                   ← business logic
│   ├── AppContext.java        ← lightweight DI container
│   ├── AuthService.java
│   ├── StudentService.java
│   ├── TeacherService.java
│   ├── CourseService.java
│   ├── GradeService.java
│   ├── EnrollmentService.java
│   ├── ScheduleService.java
│   └── AttendanceService.java
└── ui/                        ← console menus
    ├── AdminMenu.java
    ├── TeacherMenu.java
    └── StudentMenu.java
```

## Prerequisites

- Java 17+
- Maven 3.8+

## Build & Run

```bash
# Compile
mvn compile

# Run directly
mvn exec:java -Dexec.mainClass=com.sis.Main

# Build a fat JAR and run
mvn package
java -jar target/student-information-system-1.0-SNAPSHOT.jar
```

## Demo Credentials

| Role    | Username | Password  |
|---------|----------|-----------|
| Admin   | admin    | admin123  |
| Teacher | mrsmith  | teach123  |
| Teacher | msjones  | teach456  |
| Student | alice    | alice123  |
| Student | bob      | bob123    |

## Demo Data

On startup the system seeds:
- **3 courses**: CS101 (Intro to Programming), CS201 (Data Structures), MA101 (Calculus I)
- **4 schedules** across the week
- **2 students** enrolled in various courses, with grades and attendance records

Type `exit` at the login prompt to quit the application.
