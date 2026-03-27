package com.sis;

import com.sis.model.*;
import com.sis.service.AppContext;
import com.sis.ui.AdminMenu;
import com.sis.ui.StudentMenu;
import com.sis.ui.TeacherMenu;
import com.sis.util.ConsoleUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Application entry point for the Student Information System.
 *
 * <p>Bootstraps demo data and presents a login loop that routes the
 * authenticated user to their role-appropriate menu.</p>
 *
 * <h2>Demo credentials</h2>
 * <pre>
 *   Role     Username   Password
 *   ──────── ────────── ──────────
 *   Admin    admin      admin123
 *   Teacher  mrsmith    teach123
 *   Student  alice      alice123
 * </pre>
 */
public class Main {

    public static void main(String[] args) {
        AppContext ctx = new AppContext();
        seedDemoData(ctx);
        runLoginLoop(ctx);
    }

    // ---------------------------------------------------------------- login loop

    private static void runLoginLoop(AppContext ctx) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       STUDENT INFORMATION SYSTEM  (SIS v1.0)            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        boolean appRunning = true;
        while (appRunning) {
            ConsoleUtils.printHeader("Login");
            System.out.println("  Type 'exit' as username to quit.\n");
            String username = ConsoleUtils.prompt("Username");
            if (username.equalsIgnoreCase("exit")) {
                System.out.println("\nGoodbye!");
                break;
            }
            String password = ConsoleUtils.prompt("Password");

            Optional<User> userOpt = ctx.getAuthService().login(username, password);
            if (userOpt.isEmpty()) {
                ConsoleUtils.printError("Invalid credentials or inactive account. Please try again.");
                ConsoleUtils.pressEnterToContinue();
                continue;
            }

            User user = userOpt.get();
            ConsoleUtils.printSuccess("Welcome, " + user.getFullName() + "! [" + user.getRole() + "]");

            switch (user.getRole()) {
                case ADMIN   -> new AdminMenu(ctx).show();
                case TEACHER -> new TeacherMenu(ctx).show();
                case STUDENT -> new StudentMenu(ctx).show();
            }
        }
    }

    // ---------------------------------------------------------------- demo seed data

    private static void seedDemoData(AppContext ctx) {
        // ----- Admin -----
        Admin admin = new Admin("admin", "admin123",
                "System Administrator", "admin@sis.edu", "ADM-0001");
        ctx.getUserRepository().save(admin);

        // ----- Teacher -----
        Teacher teacher = new Teacher("mrsmith", "teach123",
                "John Smith", "j.smith@sis.edu",
                "TCH-0001", "Computer Science", "555-1001", "M.Sc. Computer Science");
        ctx.getTeacherService().addTeacher(teacher);

        Teacher teacher2 = new Teacher("msjones", "teach456",
                "Emily Jones", "e.jones@sis.edu",
                "TCH-0002", "Mathematics", "555-1002", "Ph.D. Mathematics");
        ctx.getTeacherService().addTeacher(teacher2);

        // ----- Courses -----
        Course cs101 = new Course("CS101", "Introduction to Programming",
                "Fundamentals of programming using Java.", 3, teacher.getId(), 30);
        ctx.getCourseService().addCourse(cs101);

        Course cs201 = new Course("CS201", "Data Structures",
                "Arrays, linked lists, trees, and graphs.", 3, teacher.getId(), 25);
        ctx.getCourseService().addCourse(cs201);

        Course ma101 = new Course("MA101", "Calculus I",
                "Limits, derivatives, and integrals.", 4, teacher2.getId(), 40);
        ctx.getCourseService().addCourse(ma101);

        // ----- Schedules -----
        ctx.getScheduleService().addSchedule(new Schedule(
                cs101.getId(), DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 30), "Room A1"));
        ctx.getScheduleService().addSchedule(new Schedule(
                cs101.getId(), DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 30), "Room A1"));
        ctx.getScheduleService().addSchedule(new Schedule(
                cs201.getId(), DayOfWeek.TUESDAY, LocalTime.of(11, 0), LocalTime.of(12, 30), "Room B2"));
        ctx.getScheduleService().addSchedule(new Schedule(
                ma101.getId(), DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(15, 30), "Room C3"));

        // ----- Students -----
        Student alice = new Student("alice", "alice123",
                "Alice Nguyen", "alice@sis.edu",
                "STU-0001", LocalDate.of(2002, 5, 15),
                "12 Maple Street", "555-2001", "Computer Science", 2023);
        ctx.getStudentService().addStudent(alice);

        Student bob = new Student("bob", "bob123",
                "Bob Patel", "bob@sis.edu",
                "STU-0002", LocalDate.of(2001, 11, 3),
                "45 Oak Avenue", "555-2002", "Mathematics", 2022);
        ctx.getStudentService().addStudent(bob);

        // ----- Enrollments -----
        ctx.getEnrollmentService().enroll(alice.getId(), cs101.getId());
        ctx.getEnrollmentService().enroll(alice.getId(), cs201.getId());
        ctx.getEnrollmentService().enroll(bob.getId(), ma101.getId());
        ctx.getEnrollmentService().enroll(bob.getId(), cs101.getId());

        // ----- Grades -----
        ctx.getGradeService().assignGrade(alice.getId(), cs101.getId(), 88.5, "Spring", 2025);
        ctx.getGradeService().assignGrade(bob.getId(),   cs101.getId(), 74.0, "Spring", 2025);
        ctx.getGradeService().assignGrade(bob.getId(),   ma101.getId(), 91.0, "Spring", 2025);

        // ----- Attendance -----
        LocalDate today = LocalDate.now();
        ctx.getAttendanceService().markAttendance(alice.getId(), cs101.getId(), today, Attendance.Status.PRESENT);
        ctx.getAttendanceService().markAttendance(alice.getId(), cs101.getId(), today.minusDays(2), Attendance.Status.PRESENT);
        ctx.getAttendanceService().markAttendance(alice.getId(), cs101.getId(), today.minusDays(4), Attendance.Status.ABSENT);
        ctx.getAttendanceService().markAttendance(bob.getId(),   cs101.getId(), today, Attendance.Status.LATE);

        ConsoleUtils.printInfo("Demo data loaded. See README for login credentials.");
    }
}
