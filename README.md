# Student Information System (SIS)

A comprehensive Java web application for managing student information, designed for educational institutions.

## Features

- **Role-based Access Control**
  - Administrator: Full CRUD for students, teachers, courses, grades
  - Teacher: Manage grades and view assigned courses
  - Student: View personal information, grades, and schedule

- **Core Modules**
  - Student Management
  - Teacher Management
  - Course Management
  - Grade Management
  - Enrollment System
  - Attendance Tracking
  - Schedule Management

## Technology Stack

- **Backend**: Java 17 + Jetty Server
- **Database**: MySQL (XAMPP)
- **Frontend**: HTML5, CSS3, JavaScript
- **Build Tool**: Maven

## Quick Start

### Prerequisites

- JDK 17 or higher
- XAMPP with MySQL (port 3308)
- Maven (included via Maven wrapper)

### Installation

1. **Start XAMPP MySQL**
   - Open XAMPP Control Panel
   - Start MySQL service

2. **Setup Database**
   ```bash
   cd JavaWebApp
   run_sql.bat
   ```

3. **Build the Application**
   ```bash
   cd JavaWebApp
   mvnw.cmd clean package -DskipTests
   ```

4. **Run the Server**
   ```bash
   java -jar target\student-information-system-web-1.0-SNAPSHOT.jar
   ```

5. **Access the Application**
   Open browser: http://localhost:8080/index.html

## Project Structure

```
student-information-system/
├── JavaWebApp/              # Main web application
│   ├── src/main/java/        # Java source code
│   │   └── com/sis/
│   │       ├── api/         # REST API Servlets
│   │       └── db/          # Database Access Objects
│   ├── src/main/resources/   # Web resources (HTML, CSS, JS)
│   ├── sisdb_complete.sql    # Database schema & sample data
│   └── pom.xml              # Maven configuration
├── README.md
├── RELEASE_NOTES.md
└── setup.bat                 # Quick setup script
```

## Login Credentials

| Role    | Username | Password   |
|---------|----------|------------|
| Admin   | admin    | admin123   |
| Teacher | mrsmith  | teach123   |
| Student | alice    | alice123   |

## API Endpoints

| Endpoint              | Method | Description              |
|----------------------|--------|--------------------------|
| `/api/login`          | POST   | User authentication      |
| `/api/logout`         | POST   | User logout             |
| `/api/students`       | GET    | List all students        |
| `/api/students`       | POST   | Add new student          |
| `/api/students/:id`   | PUT    | Update student           |
| `/api/students/:id`   | DELETE | Delete student          |
| `/api/teachers`       | GET    | List all teachers       |
| `/api/courses`        | GET    | List all courses        |
| `/api/grades`         | GET    | List all grades         |
| `/api/enrollments`    | GET    | List enrollments        |

## Database Schema

- `users` - User accounts and authentication
- `student` - Student personal information
- `techer_db` - Teacher information
- `course` - Course catalog
- `grades` - Student grades
- `enroll` - Student course enrollments
- `attendance` - Attendance records
- `schedule` - Course schedules

## Configuration

Database connection settings in `DatabaseConnection.java`:
```java
Host: localhost
Port: 3308
Database: sisdb
Username: root
Password: (empty)
```

## Development

### Using Maven Wrapper
```bash
cd JavaWebApp
mvnw.cmd clean package    # Build
mvnw.cmd test             # Run tests
```

### Manual Build
```bash
cd JavaWebApp
mvn clean package
```

## License

This project is for educational purposes.

## Author

Student Project - B.E. Computer Engineering
