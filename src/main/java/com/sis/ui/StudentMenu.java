package com.sis.ui;

import com.sis.model.*;
import com.sis.service.AppContext;
import com.sis.util.ConsoleUtils;

import java.util.List;
import java.util.Optional;

/**
 * Console menu for Student users.
 * Students can enroll/withdraw, view grades, schedule, and attendance.
 */
public class StudentMenu {

    private final AppContext ctx;

    public StudentMenu(AppContext ctx) {
        this.ctx = ctx;
    }

    public void show() {
        Student student = (Student) ctx.getAuthService().getCurrentUser();
        boolean running = true;

        while (running) {
            ConsoleUtils.printHeader("Student Portal — " + student.getFullName());
            System.out.println("  1. View My Profile");
            System.out.println("  2. Browse Available Courses");
            System.out.println("  3. Enroll in a Course");
            System.out.println("  4. Withdraw from a Course");
            System.out.println("  5. My Enrollments");
            System.out.println("  6. My Grades & GPA");
            System.out.println("  7. My Schedule");
            System.out.println("  8. My Attendance");
            System.out.println("  9. Change Password");
            System.out.println("  0. Logout");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.promptMenuChoice(0, 9);
            switch (choice) {
                case 1 -> viewProfile(student);
                case 2 -> browseAvailableCourses();
                case 3 -> enrollInCourse(student);
                case 4 -> withdrawFromCourse(student);
                case 5 -> viewMyEnrollments(student);
                case 6 -> viewMyGrades(student);
                case 7 -> viewMySchedule(student);
                case 8 -> viewMyAttendance(student);
                case 9 -> changePassword();
                case 0 -> running = false;
            }
        }
        ctx.getAuthService().logout();
        ConsoleUtils.printInfo("Logged out.");
    }

    private void viewProfile(Student student) {
        ConsoleUtils.printHeader("My Profile");
        System.out.println("  Student No    : " + student.getStudentNumber());
        System.out.println("  Name          : " + student.getFullName());
        System.out.println("  Email         : " + student.getEmail());
        System.out.println("  Phone         : " + student.getPhone());
        System.out.println("  Address       : " + student.getAddress());
        System.out.println("  Major         : " + student.getMajor());
        System.out.println("  Enroll Year   : " + student.getEnrollmentYear());
        System.out.println("  Date of Birth : " + student.getDateOfBirth());
        ConsoleUtils.pressEnterToContinue();
    }

    private void browseAvailableCourses() {
        ConsoleUtils.printHeader("Available Courses");
        List<Course> courses = ctx.getCourseService().getActiveCourses();
        if (courses.isEmpty()) {
            ConsoleUtils.printInfo("No active courses available.");
        } else {
            System.out.printf("%-8s %-30s %-8s %-12s %-10s%n",
                    "Code", "Title", "Credits", "Seats Left", "Teacher");
            ConsoleUtils.printSeparator();
            courses.forEach(c -> {
                String teacherName = ctx.getTeacherService().findById(c.getTeacherId())
                        .map(Teacher::getFullName)
                        .orElse("N/A");
                System.out.printf("%-8s %-30s %-8d %-12d %-10s%n",
                        c.getCourseCode(), c.getTitle(), c.getCredits(),
                        c.getCapacity() - c.getEnrolledCount(), teacherName);
            });
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void enrollInCourse(Student student) {
        ConsoleUtils.printHeader("Enroll in Course");
        String code = ConsoleUtils.prompt("Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(code);
        if (courseOpt.isEmpty() || !courseOpt.get().isActive()) {
            ConsoleUtils.printError("Course not found or not active.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        try {
            Enrollment e = ctx.getEnrollmentService().enroll(student.getId(), courseOpt.get().getId());
            ConsoleUtils.printSuccess("Enrolled in " + courseOpt.get().getTitle() + " on " + e.getEnrollmentDate());
        } catch (Exception e) {
            ConsoleUtils.printError(e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void withdrawFromCourse(Student student) {
        ConsoleUtils.printHeader("Withdraw from Course");
        String code = ConsoleUtils.prompt("Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(code);
        if (courseOpt.isEmpty()) {
            ConsoleUtils.printError("Course not found.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        try {
            ctx.getEnrollmentService().withdraw(student.getId(), courseOpt.get().getId());
            ConsoleUtils.printSuccess("Withdrawn from " + courseOpt.get().getTitle());
        } catch (Exception e) {
            ConsoleUtils.printError(e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewMyEnrollments(Student student) {
        ConsoleUtils.printHeader("My Enrollments");
        List<Enrollment> enrollments = ctx.getEnrollmentService().getEnrollmentsByStudent(student.getId());
        if (enrollments.isEmpty()) {
            ConsoleUtils.printInfo("You are not enrolled in any courses.");
        } else {
            System.out.printf("%-8s %-30s %-12s %-12s%n", "Code", "Title", "Date", "Status");
            ConsoleUtils.printSeparator();
            enrollments.forEach(e ->
                ctx.getCourseService().findById(e.getCourseId()).ifPresent(c ->
                    System.out.printf("%-8s %-30s %-12s %-12s%n",
                        c.getCourseCode(), c.getTitle(), e.getEnrollmentDate(), e.getStatus())));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewMyGrades(Student student) {
        ConsoleUtils.printHeader("My Grades");
        List<Grade> grades = ctx.getGradeService().getGradesByStudent(student.getId());
        if (grades.isEmpty()) {
            ConsoleUtils.printInfo("No grades available yet.");
        } else {
            System.out.printf("%-8s %-28s %-8s %-6s %-6s %-12s%n",
                    "Code", "Title", "Marks", "Grade", "GPA", "Semester");
            ConsoleUtils.printSeparator();
            grades.forEach(g ->
                ctx.getCourseService().findById(g.getCourseId()).ifPresent(c ->
                    System.out.printf("%-8s %-28s %-8.2f %-6s %-6.1f %-12s%n",
                        c.getCourseCode(), c.getTitle(),
                        g.getMarks(), g.getLetterGrade(), g.getGradePoints(),
                        g.getSemester() + " " + g.getAcademicYear())));
            double gpa = ctx.getGradeService().calculateGpa(student.getId());
            ConsoleUtils.printSeparator();
            System.out.printf("  Cumulative GPA: %.2f / 4.0%n", gpa);
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewMySchedule(Student student) {
        ConsoleUtils.printHeader("My Schedule");
        List<Enrollment> enrollments = ctx.getEnrollmentService()
                .getEnrollmentsByStudent(student.getId()).stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ENROLLED)
                .toList();
        if (enrollments.isEmpty()) {
            ConsoleUtils.printInfo("You are not enrolled in any courses.");
        } else {
            enrollments.forEach(e ->
                ctx.getCourseService().findById(e.getCourseId()).ifPresent(c -> {
                    List<Schedule> schedules = ctx.getScheduleService().getSchedulesByCourse(c.getId());
                    System.out.println("\n  " + c.getCourseCode() + " — " + c.getTitle());
                    if (schedules.isEmpty()) {
                        System.out.println("    (no schedule defined)");
                    } else {
                        schedules.forEach(s -> System.out.printf("    %-10s  %s - %s  Room: %s%n",
                                s.getDayOfWeek(), s.getStartTime(), s.getEndTime(), s.getRoom()));
                    }
                }));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewMyAttendance(Student student) {
        ConsoleUtils.printHeader("My Attendance");
        List<Enrollment> enrollments = ctx.getEnrollmentService()
                .getEnrollmentsByStudent(student.getId()).stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ENROLLED)
                .toList();
        if (enrollments.isEmpty()) {
            ConsoleUtils.printInfo("You are not enrolled in any courses.");
        } else {
            enrollments.forEach(e ->
                ctx.getCourseService().findById(e.getCourseId()).ifPresent(c -> {
                    double pct = ctx.getAttendanceService()
                            .getAttendancePercentage(student.getId(), c.getId());
                    System.out.printf("\n  %s — %s   Attendance: %.1f%%%n",
                            c.getCourseCode(), c.getTitle(), pct);
                    List<Attendance> records = ctx.getAttendanceService()
                            .getAttendanceByStudentAndCourse(student.getId(), c.getId());
                    records.forEach(a -> System.out.printf("    %-12s  %s%n", a.getDate(), a.getStatus()));
                }));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void changePassword() {
        String oldPwd  = ConsoleUtils.prompt("Current Password");
        String newPwd  = ConsoleUtils.prompt("New Password");
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
