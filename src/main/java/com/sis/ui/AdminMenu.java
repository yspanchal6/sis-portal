package com.sis.ui;

import com.sis.model.*;
import com.sis.service.*;
import com.sis.util.ConsoleUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Console menu for Admin users.
 * Admins can manage students, teachers, courses, schedules, enrollments, grades, and attendance.
 */
public class AdminMenu {

    private final AppContext ctx;

    public AdminMenu(AppContext ctx) {
        this.ctx = ctx;
    }

    public void show() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Admin Dashboard — " + ctx.getAuthService().getCurrentUser().getFullName());
            System.out.println("  1. Manage Students");
            System.out.println("  2. Manage Teachers");
            System.out.println("  3. Manage Courses");
            System.out.println("  4. Manage Schedules");
            System.out.println("  5. View All Enrollments");
            System.out.println("  6. View All Grades");
            System.out.println("  7. View All Attendance");
            System.out.println("  8. Change Password");
            System.out.println("  0. Logout");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.promptMenuChoice(0, 8);
            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageTeachers();
                case 3 -> manageCourses();
                case 4 -> manageSchedules();
                case 5 -> viewAllEnrollments();
                case 6 -> viewAllGrades();
                case 7 -> viewAllAttendance();
                case 8 -> changePassword();
                case 0 -> running = false;
            }
        }
        ctx.getAuthService().logout();
        ConsoleUtils.printInfo("Logged out.");
    }

    // ---------------------------------------------------------------- students

    private void manageStudents() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.printHeader("Manage Students");
            System.out.println("  1. List All Students");
            System.out.println("  2. Add New Student");
            System.out.println("  3. View Student Detail");
            System.out.println("  4. Deactivate Student");
            System.out.println("  0. Back");
            ConsoleUtils.printSeparator();

            switch (ConsoleUtils.promptMenuChoice(0, 4)) {
                case 1 -> listAllStudents();
                case 2 -> addStudent();
                case 3 -> viewStudentDetail();
                case 4 -> deactivateStudent();
                case 0 -> back = true;
            }
        }
    }

    private void listAllStudents() {
        ConsoleUtils.printHeader("All Students");
        List<Student> students = ctx.getStudentService().getAllStudents();
        if (students.isEmpty()) {
            ConsoleUtils.printInfo("No students registered.");
        } else {
            System.out.printf("%-12s %-25s %-20s %-6s %-8s%n",
                    "StudentNo", "Name", "Major", "Year", "Active");
            ConsoleUtils.printSeparator();
            students.forEach(s -> System.out.printf("%-12s %-25s %-20s %-6d %-8s%n",
                    s.getStudentNumber(), s.getFullName(), s.getMajor(),
                    s.getEnrollmentYear(), s.isActive() ? "Yes" : "No"));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void addStudent() {
        ConsoleUtils.printHeader("Add New Student");
        String username    = ConsoleUtils.prompt("Username");
        String password    = ConsoleUtils.prompt("Password");
        String fullName    = ConsoleUtils.prompt("Full Name");
        String email       = ConsoleUtils.prompt("Email");
        String studentNum  = ConsoleUtils.prompt("Student Number (e.g. STU-0001)");
        String dob         = ConsoleUtils.prompt("Date of Birth (YYYY-MM-DD)");
        String address     = ConsoleUtils.prompt("Address");
        String phone       = ConsoleUtils.prompt("Phone");
        String major       = ConsoleUtils.prompt("Major");
        int    year        = ConsoleUtils.promptInt("Enrollment Year");

        try {
            LocalDate dateOfBirth = LocalDate.parse(dob);
            Student student = new Student(username, password, fullName, email,
                    studentNum, dateOfBirth, address, phone, major, year);
            ctx.getStudentService().addStudent(student);
            ConsoleUtils.printSuccess("Student added: " + student.getFullName());
        } catch (DateTimeParseException e) {
            ConsoleUtils.printError("Invalid date format. Use YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            ConsoleUtils.printError(e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewStudentDetail() {
        String num = ConsoleUtils.prompt("Enter Student Number");
        ctx.getStudentService().findByStudentNumber(num).ifPresentOrElse(
                s -> {
                    ConsoleUtils.printHeader("Student Detail");
                    System.out.println("  ID            : " + s.getId());
                    System.out.println("  Student No    : " + s.getStudentNumber());
                    System.out.println("  Name          : " + s.getFullName());
                    System.out.println("  Email         : " + s.getEmail());
                    System.out.println("  Phone         : " + s.getPhone());
                    System.out.println("  Address       : " + s.getAddress());
                    System.out.println("  Major         : " + s.getMajor());
                    System.out.println("  Enroll Year   : " + s.getEnrollmentYear());
                    System.out.println("  DOB           : " + s.getDateOfBirth());
                    System.out.println("  Active        : " + s.isActive());
                },
                () -> ConsoleUtils.printError("Student not found.")
        );
        ConsoleUtils.pressEnterToContinue();
    }

    private void deactivateStudent() {
        String num = ConsoleUtils.prompt("Enter Student Number to deactivate");
        ctx.getStudentService().findByStudentNumber(num).ifPresentOrElse(
                s -> {
                    ctx.getStudentService().deactivateStudent(s.getId());
                    ConsoleUtils.printSuccess("Student deactivated: " + s.getFullName());
                },
                () -> ConsoleUtils.printError("Student not found.")
        );
        ConsoleUtils.pressEnterToContinue();
    }

    // ---------------------------------------------------------------- teachers

    private void manageTeachers() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.printHeader("Manage Teachers");
            System.out.println("  1. List All Teachers");
            System.out.println("  2. Add New Teacher");
            System.out.println("  3. Deactivate Teacher");
            System.out.println("  0. Back");
            ConsoleUtils.printSeparator();

            switch (ConsoleUtils.promptMenuChoice(0, 3)) {
                case 1 -> listAllTeachers();
                case 2 -> addTeacher();
                case 3 -> deactivateTeacher();
                case 0 -> back = true;
            }
        }
    }

    private void listAllTeachers() {
        ConsoleUtils.printHeader("All Teachers");
        List<Teacher> teachers = ctx.getTeacherService().getAllTeachers();
        if (teachers.isEmpty()) {
            ConsoleUtils.printInfo("No teachers registered.");
        } else {
            System.out.printf("%-12s %-25s %-20s %-8s%n", "TeacherNo", "Name", "Department", "Active");
            ConsoleUtils.printSeparator();
            teachers.forEach(t -> System.out.printf("%-12s %-25s %-20s %-8s%n",
                    t.getTeacherNumber(), t.getFullName(), t.getDepartment(),
                    t.isActive() ? "Yes" : "No"));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void addTeacher() {
        ConsoleUtils.printHeader("Add New Teacher");
        String username      = ConsoleUtils.prompt("Username");
        String password      = ConsoleUtils.prompt("Password");
        String fullName      = ConsoleUtils.prompt("Full Name");
        String email         = ConsoleUtils.prompt("Email");
        String teacherNum    = ConsoleUtils.prompt("Teacher Number (e.g. TCH-0001)");
        String department    = ConsoleUtils.prompt("Department");
        String phone         = ConsoleUtils.prompt("Phone");
        String qualification = ConsoleUtils.prompt("Qualification");

        try {
            Teacher teacher = new Teacher(username, password, fullName, email,
                    teacherNum, department, phone, qualification);
            ctx.getTeacherService().addTeacher(teacher);
            ConsoleUtils.printSuccess("Teacher added: " + teacher.getFullName());
        } catch (IllegalArgumentException e) {
            ConsoleUtils.printError(e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void deactivateTeacher() {
        String num = ConsoleUtils.prompt("Enter Teacher Number to deactivate");
        ctx.getTeacherService().findByTeacherNumber(num).ifPresentOrElse(
                t -> {
                    ctx.getTeacherService().deactivateTeacher(t.getId());
                    ConsoleUtils.printSuccess("Teacher deactivated: " + t.getFullName());
                },
                () -> ConsoleUtils.printError("Teacher not found.")
        );
        ConsoleUtils.pressEnterToContinue();
    }

    // ---------------------------------------------------------------- courses

    private void manageCourses() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.printHeader("Manage Courses");
            System.out.println("  1. List All Courses");
            System.out.println("  2. Add New Course");
            System.out.println("  3. Deactivate Course");
            System.out.println("  0. Back");
            ConsoleUtils.printSeparator();

            switch (ConsoleUtils.promptMenuChoice(0, 3)) {
                case 1 -> listAllCourses();
                case 2 -> addCourse();
                case 3 -> deactivateCourse();
                case 0 -> back = true;
            }
        }
    }

    private void listAllCourses() {
        ConsoleUtils.printHeader("All Courses");
        List<Course> courses = ctx.getCourseService().getAllCourses();
        if (courses.isEmpty()) {
            ConsoleUtils.printInfo("No courses available.");
        } else {
            System.out.printf("%-8s %-30s %-8s %-12s %-8s%n",
                    "Code", "Title", "Credits", "Enrolled", "Active");
            ConsoleUtils.printSeparator();
            courses.forEach(c -> System.out.printf("%-8s %-30s %-8d %-12s %-8s%n",
                    c.getCourseCode(), c.getTitle(), c.getCredits(),
                    c.getEnrolledCount() + "/" + c.getCapacity(),
                    c.isActive() ? "Yes" : "No"));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void addCourse() {
        ConsoleUtils.printHeader("Add New Course");
        String code        = ConsoleUtils.prompt("Course Code (e.g. CS101)");
        String title       = ConsoleUtils.prompt("Title");
        String description = ConsoleUtils.prompt("Description");
        int    credits     = ConsoleUtils.promptInt("Credits");
        String teacherNum  = ConsoleUtils.prompt("Teacher Number");
        int    capacity    = ConsoleUtils.promptInt("Capacity");

        try {
            Teacher teacher = ctx.getTeacherService().findByTeacherNumber(teacherNum)
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherNum));
            Course course = new Course(code, title, description, credits, teacher.getId(), capacity);
            ctx.getCourseService().addCourse(course);
            ConsoleUtils.printSuccess("Course added: " + course.getTitle());
        } catch (IllegalArgumentException e) {
            ConsoleUtils.printError(e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void deactivateCourse() {
        String code = ConsoleUtils.prompt("Enter Course Code to deactivate");
        ctx.getCourseService().findByCourseCode(code).ifPresentOrElse(
                c -> {
                    ctx.getCourseService().deactivateCourse(c.getId());
                    ConsoleUtils.printSuccess("Course deactivated: " + c.getTitle());
                },
                () -> ConsoleUtils.printError("Course not found.")
        );
        ConsoleUtils.pressEnterToContinue();
    }

    // ---------------------------------------------------------------- schedules

    private void manageSchedules() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.printHeader("Manage Schedules");
            System.out.println("  1. List All Schedules");
            System.out.println("  2. Add Schedule");
            System.out.println("  0. Back");
            ConsoleUtils.printSeparator();

            switch (ConsoleUtils.promptMenuChoice(0, 2)) {
                case 1 -> listAllSchedules();
                case 2 -> addSchedule();
                case 0 -> back = true;
            }
        }
    }

    private void listAllSchedules() {
        ConsoleUtils.printHeader("All Schedules");
        List<Schedule> schedules = ctx.getScheduleService().getAllSchedules();
        if (schedules.isEmpty()) {
            ConsoleUtils.printInfo("No schedules defined.");
        } else {
            System.out.printf("%-10s %-12s %-8s %-8s %-10s%n",
                    "CourseId", "Day", "Start", "End", "Room");
            ConsoleUtils.printSeparator();
            schedules.forEach(s -> System.out.printf("%-10s %-12s %-8s %-8s %-10s%n",
                    s.getCourseId().substring(0, Math.min(10, s.getCourseId().length())),
                    s.getDayOfWeek(), s.getStartTime(), s.getEndTime(), s.getRoom()));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void addSchedule() {
        ConsoleUtils.printHeader("Add Schedule");
        String courseCode = ConsoleUtils.prompt("Course Code");
        String dayStr     = ConsoleUtils.prompt("Day of Week (e.g. MONDAY)");
        String startStr   = ConsoleUtils.prompt("Start Time (HH:MM)");
        String endStr     = ConsoleUtils.prompt("End Time   (HH:MM)");
        String room       = ConsoleUtils.prompt("Room");

        try {
            Course course = ctx.getCourseService().findByCourseCode(courseCode)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseCode));
            DayOfWeek day   = DayOfWeek.valueOf(dayStr.toUpperCase());
            LocalTime start = LocalTime.parse(startStr);
            LocalTime end   = LocalTime.parse(endStr);
            Schedule  sched = new Schedule(course.getId(), day, start, end, room);
            ctx.getScheduleService().addSchedule(sched);
            ConsoleUtils.printSuccess("Schedule added.");
        } catch (Exception e) {
            ConsoleUtils.printError("Error: " + e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    // ---------------------------------------------------------------- view-only sections

    private void viewAllEnrollments() {
        ConsoleUtils.printHeader("All Enrollments");
        List<Enrollment> enrollments = ctx.getEnrollmentService().getAllEnrollments();
        if (enrollments.isEmpty()) {
            ConsoleUtils.printInfo("No enrollments found.");
        } else {
            System.out.printf("%-36s %-36s %-12s %-12s%n",
                    "StudentId", "CourseId", "Date", "Status");
            ConsoleUtils.printSeparator();
            enrollments.forEach(e -> System.out.printf("%-36s %-36s %-12s %-12s%n",
                    e.getStudentId(), e.getCourseId(), e.getEnrollmentDate(), e.getStatus()));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewAllGrades() {
        ConsoleUtils.printHeader("All Grades");
        List<Course> courses = ctx.getCourseService().getAllCourses();
        courses.forEach(c -> {
            List<Grade> grades = ctx.getGradeService().getGradesByCourse(c.getId());
            if (!grades.isEmpty()) {
                System.out.println("\nCourse: " + c.getCourseCode() + " — " + c.getTitle());
                grades.forEach(g -> System.out.printf("  StudentId=%-36s  Marks=%-6.2f  Grade=%s%n",
                        g.getStudentId(), g.getMarks(), g.getLetterGrade()));
            }
        });
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewAllAttendance() {
        ConsoleUtils.printHeader("All Attendance Records");
        List<Attendance> records = ctx.getAttendanceService()
                .getAttendanceByCourse(""); // we'll list per course instead
        // List attendance by course
        List<Course> courses = ctx.getCourseService().getAllCourses();
        courses.forEach(c -> {
            List<Attendance> atts = ctx.getAttendanceService().getAttendanceByCourse(c.getId());
            if (!atts.isEmpty()) {
                System.out.println("\nCourse: " + c.getCourseCode() + " — " + c.getTitle());
                atts.forEach(a -> System.out.printf("  StudentId=%-36s  Date=%-12s  Status=%s%n",
                        a.getStudentId(), a.getDate(), a.getStatus()));
            }
        });
        ConsoleUtils.pressEnterToContinue();
    }

    // ---------------------------------------------------------------- password

    private void changePassword() {
        String oldPwd = ConsoleUtils.prompt("Current Password");
        String newPwd = ConsoleUtils.prompt("New Password");
        String confirm = ConsoleUtils.prompt("Confirm New Password");
        if (!newPwd.equals(confirm)) {
            ConsoleUtils.printError("Passwords do not match.");
        } else {
            boolean ok = ctx.getAuthService().changePassword(
                    ctx.getAuthService().getCurrentUser().getId(), oldPwd, newPwd);
            if (ok) ConsoleUtils.printSuccess("Password changed.");
            else    ConsoleUtils.printError("Incorrect current password.");
        }
        ConsoleUtils.pressEnterToContinue();
    }
}
