# Student Information System (SIS) - Project Report

**Project Title:** Student Information System (Web-Based)  
**Version:** 1.0.0  
**Date:** May 2026  
**Institution:** B.E Year 2, Semester 4  

---

## 1. Executive Summary

The Student Information System (SIS) is a comprehensive Java-based web application designed to manage student information, teacher records, courses, grades, and enrollment in educational institutions. This project demonstrates the migration from a desktop GUI application to a modern web-based architecture, utilizing REST API principles and a MySQL database backend.

---

## 2. Project Overview

### 2.1 Objectives
- Develop a centralized system for managing student, teacher, and course information
- Implement role-based access control for different user types (Admin, Teacher, Student)
- Provide a user-friendly web interface for data management
- Enable grade tracking and enrollment management
- Create a scalable, maintainable architecture using modern Java technologies

### 2.2 Scope
The system provides functionality for:
- **Student Management**: View, add, edit, and delete student records
- **Teacher Management**: Manage teacher profiles and department assignments
- **Course Management**: Maintain course catalog and assignments
- **Grade Management**: Track and manage student grades
- **Enrollment System**: Handle student course enrollments
- **Dashboard**: Provide role-specific dashboards with relevant information

---

## 3. System Architecture

### 3.1 Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│              Frontend (HTML/CSS/JavaScript)             │
│         (HTML Files + CSS Styling + API Calls)          │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ HTTP Requests/Responses
                     │ JSON Data Exchange
                     ↓
┌─────────────────────────────────────────────────────────┐
│           Backend API Layer (Jetty Servlets)            │
│  (LoginServlet, StudentServlet, TeacherServlet, etc.)   │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ JDBC
                     │ Data Access Operations
                     ↓
┌─────────────────────────────────────────────────────────┐
│        Data Access Layer (DAO Pattern)                  │
│  (StudentDAO, TeacherDAO, CourseDAO, GradeDAO, etc.)   │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ JDBC Connection
                     │ SQL Queries
                     ↓
┌─────────────────────────────────────────────────────────┐
│              MySQL Database                             │
│         (8 Tables with Relationships)                   │
└─────────────────────────────────────────────────────────┘
```

### 3.2 Design Patterns Used
- **MVC (Model-View-Controller)**: Separation of concerns with Frontend, Servlet Layer, and Database Layer
- **DAO (Data Access Object)**: Encapsulates database operations in dedicated classes
- **Singleton Pattern**: Database connection management
- **Servlet Pattern**: RESTful API endpoints for client-server communication

---

## 4. Technology Stack

### 4.1 Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17+ | Programming Language |
| Jetty | 11.0.18 | Embedded Web Server |
| MySQL | 8.0+ | Database |
| MySQL Connector | 8.0.33 | JDBC Driver |
| Gson | 2.10.1 | JSON Processing |
| Maven | Latest | Build Automation |

### 4.2 Frontend
| Technology | Purpose |
|-----------|---------|
| HTML5 | Page Structure |
| CSS3 | Styling & Responsive Design |
| JavaScript (Vanilla) | Client-side Logic & API Integration |

### 4.3 Build & Deployment
- **Build Tool**: Apache Maven
- **JAR Packaging**: Maven Shade Plugin
- **Execution**: Standalone JAR file
- **Server Runtime**: Embedded Jetty (No external server needed)

---

## 5. Project Structure

```
student-information-system/
├── JavaWebApp/                          # Main application directory
│   ├── src/main/
│   │   ├── java/com/sis/
│   │   │   ├── Main.java               # Application entry point
│   │   │   ├── api/                    # Servlet controllers
│   │   │   │   ├── LoginServlet.java
│   │   │   │   ├── LogoutServlet.java
│   │   │   │   ├── StudentServlet.java
│   │   │   │   ├── TeacherServlet.java
│   │   │   │   ├── CourseServlet.java
│   │   │   │   ├── GradeServlet.java
│   │   │   │   ├── EnrollmentServlet.java
│   │   │   │   ├── DashboardServlet.java
│   │   │   │   └── StaticServlet.java
│   │   │   ├── db/                     # Data Access Objects
│   │   │   │   ├── DatabaseConnection.java
│   │   │   │   ├── UserDAO.java
│   │   │   │   ├── StudentDAO.java
│   │   │   │   ├── TeacherDAO.java
│   │   │   │   ├── CourseDAO.java
│   │   │   │   ├── GradeDAO.java
│   │   │   │   └── EnrollmentDAO.java
│   │   │   └── model/                  # Entity models
│   │   └── resources/webapp/           # Frontend files
│   │       ├── index.html
│   │       ├── login.html
│   │       ├── admin.html
│   │       ├── student.html
│   │       ├── teacher.html
│   │       ├── css/
│   │       │   └── style.css
│   │       └── js/
│   │           ├── api.js
│   │           ├── login.js
│   │           ├── admin.js
│   │           ├── student.js
│   │           └── teacher.js
│   ├── pom.xml                         # Maven configuration
│   ├── mvnw & mvnw.cmd                 # Maven wrapper
│   └── sisdb_complete.sql              # Database schema
├── setup.bat                            # Automated setup script
├── README.md                            # User documentation
├── RELEASE_NOTES.md                     # Version history
└── LICENSE                              # Project license
```

---

## 6. Component Descriptions

### 6.1 Servlet Layer (API Endpoints)

| Servlet | Endpoint | Methods | Purpose |
|---------|----------|---------|---------|
| **LoginServlet** | `/api/login` | POST | User authentication |
| **LogoutServlet** | `/api/logout` | POST | User session termination |
| **StudentServlet** | `/api/students/*` | GET, POST, PUT, DELETE | Student CRUD operations |
| **TeacherServlet** | `/api/teachers/*` | GET, POST, PUT, DELETE | Teacher management |
| **CourseServlet** | `/api/courses/*` | GET, POST, PUT, DELETE | Course catalog management |
| **GradeServlet** | `/api/grades/*` | GET, POST, PUT, DELETE | Grade tracking |
| **EnrollmentServlet** | `/api/enrollments/*` | GET, POST, PUT, DELETE | Student enrollments |
| **DashboardServlet** | `/api/dashboard/*` | GET | Role-specific dashboards |
| **StaticServlet** | `/api/static/*` | GET | Static file serving |

### 6.2 Data Access Layer (DAOs)

**DatabaseConnection.java**
- Manages MySQL database connectivity
- Singleton pattern for connection pooling
- Handles connection establishment and cleanup

**UserDAO.java**
- Authentication and user credential verification
- User role management
- Session management

**StudentDAO.java**
- CRUD operations for student records
- Student search and filtering
- Enrollment count tracking

**TeacherDAO.java**
- Teacher profile management
- Department assignment
- Course assignment

**CourseDAO.java**
- Course catalog management
- Capacity tracking
- Teacher assignment to courses

**GradeDAO.java**
- Grade entry and retrieval
- Grade aggregation (average, total marks)
- Semester/year-based filtering

**EnrollmentDAO.java**
- Enrollment creation and removal
- Enrollment status tracking
- Enrollment list retrieval per course/student

### 6.3 Frontend Pages

**index.html**
- Landing/home page
- Project information and navigation

**login.html**
- User authentication interface
- Role selection for login (Admin/Teacher/Student)

**admin.html**
- Admin dashboard
- Student management interface
- Teacher management interface
- Course management interface

**student.html**
- Student dashboard
- Course enrollment interface
- Grade view
- Personal information

**teacher.html**
- Teacher dashboard
- Grade entry interface
- Class management
- Student list for assigned courses

---

## 7. Database Schema

### 7.1 Entity-Relationship Overview

```
┌──────────────────┐
│      Users       │
│  (Credentials)   │
└────────┬─────────┘
         │
    ┌────┴────┬─────────────────┐
    │          │                 │
    ↓          ↓                 ↓
┌────────┐ ┌────────┐      ┌──────────┐
│Students│ │Teachers│      │Enrollments
│        │ │        │      │          │
└────┬───┘ └────┬───┘      └────┬─────┘
     │          │               │
     │          ↓               │
     │      ┌─────────┐         │
     │      │ Courses │←────────┘
     │      └────┬────┘
     │           │
     ↓           ↓
  ┌──────────────────┐
  │      Grades      │
  │   (CourseID,     │
  │    StudentID)    │
  └──────────────────┘
```

### 7.2 Main Tables

1. **users** - Authentication and user credentials
2. **students** - Student profiles and information
3. **teachers** - Teacher profiles and department
4. **courses** - Course catalog and details
5. **enrollments** - Student course enrollments
6. **grades** - Student grades and marks
7. **schedule** - Course scheduling information
8. **attendance** - Daily attendance records

---

## 8. Features

### 8.1 User Authentication
- ✅ Role-based login (Admin, Teacher, Student)
- ✅ Session management
- ✅ Logout functionality
- ✅ Password verification

### 8.2 Student Management
- ✅ View all students
- ✅ Add new student records
- ✅ Edit student information
- ✅ Delete student records
- ✅ Search student by ID/name

### 8.3 Teacher Management
- ✅ Manage teacher profiles
- ✅ Assign departments
- ✅ Course assignments
- ✅ View teacher details

### 8.4 Course Management
- ✅ Maintain course catalog
- ✅ Assign teachers to courses
- ✅ Track course capacity
- ✅ Monitor enrollment counts

### 8.5 Grade Management
- ✅ Enter student grades
- ✅ View grade reports
- ✅ Grade aggregation (average, total)
- ✅ Organize by semester/year

### 8.6 Enrollment System
- ✅ Student course enrollment
- ✅ Enrollment status tracking
- ✅ Drop course functionality
- ✅ Automatic enrollment counting

### 8.7 Dashboard
- ✅ Admin dashboard (system overview)
- ✅ Teacher dashboard (class management)
- ✅ Student dashboard (personal records)

---

## 9. How to Run the Project

### 9.1 Prerequisites
- Java Development Kit (JDK) 17 or higher
- MySQL Server (running on port 3306)
- Maven (or use included Maven wrapper)
- Windows/Linux/macOS

### 9.2 Setup Steps

**Step 1: Set Java Home (Windows)**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

**Step 2: Navigate to Project**
```powershell
cd JavaWebApp
```

**Step 3: Build the Project**
```powershell
.\mvnw.cmd clean package -DskipTests
```

**Step 4: Run the Application**
```powershell
java -jar target\student-information-system-web-1.0-SNAPSHOT.jar
```

**Step 5: Access the Application**
- Open browser: `http://localhost:8080/index.html`

### 9.3 Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Teacher | mrsmith | teach123 |
| Student | alice | alice123 |

---

## 10. API Endpoints Summary

### Authentication
- `POST /api/login` - User login
- `POST /api/logout` - User logout

### Students
- `GET /api/students/` - List all students
- `GET /api/students/{id}` - Get student details
- `POST /api/students/` - Add new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### Teachers
- `GET /api/teachers/` - List all teachers
- `GET /api/teachers/{id}` - Get teacher details
- `POST /api/teachers/` - Add new teacher
- `PUT /api/teachers/{id}` - Update teacher
- `DELETE /api/teachers/{id}` - Delete teacher

### Courses
- `GET /api/courses/` - List all courses
- `GET /api/courses/{id}` - Get course details
- `POST /api/courses/` - Add new course
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

### Grades
- `GET /api/grades/` - List grades
- `POST /api/grades/` - Enter grade
- `PUT /api/grades/{id}` - Update grade
- `DELETE /api/grades/{id}` - Delete grade

### Enrollments
- `GET /api/enrollments/` - List enrollments
- `POST /api/enrollments/` - Create enrollment
- `DELETE /api/enrollments/{id}` - Remove enrollment

---

## 11. Security Features

### 11.1 Authentication
- Username and password verification
- Session-based authentication
- Role-based access control

### 11.2 Data Protection
- Secure database connections
- SQL operations through prepared statements
- User input validation

---

## 12. Performance Considerations

### 12.1 Database Optimization
- Indexed key fields for faster queries
- Proper foreign key relationships
- Efficient JOIN operations

### 12.2 Caching
- Session caching for user data
- Static file serving with caching

### 12.3 Scalability
- Stateless servlet design
- Connection pooling ready
- REST API for distributed clients

---

## 13. Future Enhancements

### Phase 2 (Planned)
- [ ] Password change functionality
- [ ] Email notifications
- [ ] PDF/Excel report generation
- [ ] Attendance reports
- [ ] Student profile pictures
- [ ] Mobile responsive design improvements
- [ ] Real-time notifications
- [ ] Advanced search and filtering
- [ ] Bulk import/export (CSV)
- [ ] Audit logging

### Phase 3 (Long-term)
- [ ] Mobile application (Android/iOS)
- [ ] Advanced analytics dashboard
- [ ] Payment integration
- [ ] Video conferencing integration
- [ ] Learning management system (LMS) features

---

## 14. Known Issues & Resolutions

### Resolved Issues
- ✅ SQL connection issues - Fixed with proper JDBC configuration
- ✅ Port configuration - Fixed default port to 8080
- ✅ Missing fields in INSERT queries - All required fields mapped

### Current Status
- No critical issues reported
- System is production-ready for educational use

---

## 15. Testing

### 15.1 Manual Testing Performed
- User login with multiple roles
- CRUD operations for all modules
- Search and filter functionality
- Grade entry and retrieval
- Enrollment workflows
- Dashboard rendering

### 15.2 Data Validation
- Input validation in servlets
- Database constraint checks
- Foreign key relationship verification

---

## 16. Deployment

### 16.1 Deployment Method
- Standalone JAR file execution
- Embedded Jetty server
- No external web server required

### 16.2 System Requirements
- **OS**: Windows, Linux, macOS
- **RAM**: Minimum 512MB (1GB recommended)
- **Disk Space**: 200MB
- **Network**: Internet for database connectivity

### 16.3 Configuration
- Default port: 8080
- Database port: 3306
- Max connections: Configurable in DatabaseConnection.java

---

## 17. Maintenance & Support

### 17.1 Code Documentation
- Inline comments for complex logic
- Class-level documentation
- SQL schema documentation

### 17.2 Logging
- Server startup messages
- Error logging in console
- Database operation tracking

### 17.3 Backup Strategy
- Regular database backups recommended
- SQL dump available in `sisdb_complete.sql`

---

## 18. Conclusion

The Student Information System is a fully functional, production-ready web application that demonstrates modern Java development practices. It successfully migrated from a desktop GUI to a scalable web architecture while maintaining all core functionality. The system is well-architected, properly documented, and ready for educational institution deployment.

### Key Achievements
✅ Complete REST API implementation  
✅ Role-based access control  
✅ Comprehensive database schema  
✅ User-friendly web interface  
✅ Standalone JAR deployment  
✅ All CRUD operations functional  

---

**Project Status:** ✅ **COMPLETE**  
**Last Updated:** May 5, 2026  
**Version:** 1.0.0

---

## Appendix: Quick Reference

### Build Commands
```bash
# Clean build
.\mvnw.cmd clean package -DskipTests

# Run application
java -jar target\student-information-system-web-1.0-SNAPSHOT.jar
```

### Database Reset
```bash
# Run the SQL script to reset database
.\run_sql.bat
```

### File Locations
- **Frontend**: `JavaWebApp/src/main/resources/webapp/`
- **Backend**: `JavaWebApp/src/main/java/com/sis/`
- **Database Script**: `JavaWebApp/sisdb_complete.sql`
- **Build Output**: `JavaWebApp/target/`

---

*This report is auto-generated and accurate as of May 5, 2026*
