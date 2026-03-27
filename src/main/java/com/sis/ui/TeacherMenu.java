package com.sis.ui;

import com.sis.model.*;
import com.sis.service.AppContext;
import com.sis.util.ConsoleUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Console menu for Teacher users.
 * Teachers can manage grades, attendance, and view courses/schedules.
 */
public class TeacherMenu {

    private final AppContext ctx;

    public TeacherMenu(AppContext ctx) {
        this.ctx = ctx;
    }

    public void show() {
        Teacher teacher = (Teacher) ctx.getAuthService().getCurrentUser();
        boolean running = true;

        while (running) {
            ConsoleUtils.printHeader("Teacher Dashboard — " + teacher.getFullName());
            System.out.println("  1. View My Courses");
            System.out.println("  2. View Enrolled Students");
            System.out.println("  3. Assign / Update Grade");
            System.out.println("  4. View Grades for a Course");
            System.out.println("  5. Mark Attendance");
            System.out.println("  6. View Attendance for a Course");
            System.out.println("  7. View Course Schedule");
            System.out.println("  8. Change Password");
            System.out.println("  0. Logout");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.promptMenuChoice(0, 8);
            switch (choice) {
                case 1 -> viewMyCourses(teacher);
                case 2 -> viewEnrolledStudents(teacher);
                case 3 -> assignGrade(teacher);
                case 4 -> viewGradesForCourse(teacher);
                case 5 -> markAttendance(teacher);
                case 6 -> viewAttendanceForCourse(teacher);
                case 7 -> viewCourseSchedule(teacher);
                case 8 -> changePassword();
                case 0 -> running = false;
            }
        }
        ctx.getAuthService().logout();
        ConsoleUtils.printInfo("Logged out.");
    }

    private void viewMyCourses(Teacher teacher) {
        ConsoleUtils.printHeader("My Courses");
        List<Course> courses = ctx.getCourseService().getCoursesByTeacher(teacher.getId());
        if (courses.isEmpty()) {
            ConsoleUtils.printInfo("No courses assigned to you.");
        } else {
            System.out.printf("%-8s %-30s %-8s %-12s%n", "Code", "Title", "Credits", "Enrolled");
            ConsoleUtils.printSeparator();
            courses.forEach(c -> System.out.printf("%-8s %-30s %-8d %-12s%n",
                    c.getCourseCode(), c.getTitle(), c.getCredits(),
                    c.getEnrolledCount() + "/" + c.getCapacity()));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewEnrolledStudents(Teacher teacher) {
        String code = ConsoleUtils.prompt("Enter Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(code);
        if (courseOpt.isEmpty() || !courseOpt.get().getTeacherId().equals(teacher.getId())) {
            ConsoleUtils.printError("Course not found or not assigned to you.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        Course course = courseOpt.get();
        ConsoleUtils.printHeader("Enrolled Students — " + course.getTitle());
        List<Enrollment> enrollments = ctx.getEnrollmentService().getEnrollmentsByCourse(course.getId());
        enrollments.stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ENROLLED)
                .forEach(e -> {
                    ctx.getStudentService().findById(e.getStudentId()).ifPresent(s ->
                            System.out.printf("  %-12s %-25s %-20s%n",
                                    s.getStudentNumber(), s.getFullName(), s.getMajor()));
                });
        ConsoleUtils.pressEnterToContinue();
    }

    private void assignGrade(Teacher teacher) {
        ConsoleUtils.printHeader("Assign / Update Grade");
        String courseCode = ConsoleUtils.prompt("Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(courseCode);
        if (courseOpt.isEmpty() || !courseOpt.get().getTeacherId().equals(teacher.getId())) {
            ConsoleUtils.printError("Course not found or not assigned to you.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        Course course = courseOpt.get();
        String studentNum = ConsoleUtils.prompt("Student Number");
        Optional<Student> studentOpt = ctx.getStudentService().findByStudentNumber(studentNum);
        if (studentOpt.isEmpty()) {
            ConsoleUtils.printError("Student not found.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        Student student   = studentOpt.get();
        double marks      = ConsoleUtils.promptDouble("Marks (0-100)");
        String semester   = ConsoleUtils.prompt("Semester (e.g. Fall)");
        int    year       = ConsoleUtils.promptInt("Academic Year (e.g. 2025)");

        try {
            Grade grade = ctx.getGradeService().assignGrade(
                    student.getId(), course.getId(), marks, semester, year);
            ConsoleUtils.printSuccess(String.format("Grade assigned: %s → %.2f (%s)",
                    student.getFullName(), grade.getMarks(), grade.getLetterGrade()));
        } catch (IllegalStateException e) {
            ConsoleUtils.printError(e.getMessage());
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewGradesForCourse(Teacher teacher) {
        String code = ConsoleUtils.prompt("Enter Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(code);
        if (courseOpt.isEmpty() || !courseOpt.get().getTeacherId().equals(teacher.getId())) {
            ConsoleUtils.printError("Course not found or not assigned to you.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        Course course = courseOpt.get();
        ConsoleUtils.printHeader("Grades — " + course.getTitle());
        List<Grade> grades = ctx.getGradeService().getGradesByCourse(course.getId());
        if (grades.isEmpty()) {
            ConsoleUtils.printInfo("No grades recorded yet.");
        } else {
            System.out.printf("%-12s %-25s %-8s %-6s %-10s%n",
                    "StudentNo", "Name", "Marks", "Grade", "GPA Points");
            ConsoleUtils.printSeparator();
            grades.forEach(g ->
                ctx.getStudentService().findById(g.getStudentId()).ifPresent(s ->
                    System.out.printf("%-12s %-25s %-8.2f %-6s %-10.1f%n",
                        s.getStudentNumber(), s.getFullName(),
                        g.getMarks(), g.getLetterGrade(), g.getGradePoints())));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void markAttendance(Teacher teacher) {
        ConsoleUtils.printHeader("Mark Attendance");
        String courseCode = ConsoleUtils.prompt("Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(courseCode);
        if (courseOpt.isEmpty() || !courseOpt.get().getTeacherId().equals(teacher.getId())) {
            ConsoleUtils.printError("Course not found or not assigned to you.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        Course course     = courseOpt.get();
        String dateStr    = ConsoleUtils.prompt("Date (YYYY-MM-DD, blank = today)");
        LocalDate date;
        try {
            date = dateStr.isBlank() ? LocalDate.now() : LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            ConsoleUtils.printError("Invalid date format.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }

        // List enrolled students and mark each
        List<Enrollment> enrollments = ctx.getEnrollmentService()
                .getEnrollmentsByCourse(course.getId()).stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ENROLLED)
                .toList();

        if (enrollments.isEmpty()) {
            ConsoleUtils.printInfo("No enrolled students.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }

        for (Enrollment e : enrollments) {
            ctx.getStudentService().findById(e.getStudentId()).ifPresent(s -> {
                System.out.printf("%s (%s) — Enter status [P=Present, A=Absent, L=Late]: ",
                        s.getFullName(), s.getStudentNumber());
                String input = ConsoleUtils.getScanner().nextLine().trim().toUpperCase();
                Attendance.Status status = switch (input) {
                    case "P" -> Attendance.Status.PRESENT;
                    case "A" -> Attendance.Status.ABSENT;
                    case "L" -> Attendance.Status.LATE;
                    default  -> Attendance.Status.ABSENT;
                };
                try {
                    ctx.getAttendanceService().markAttendance(s.getId(), course.getId(), date, status);
                } catch (IllegalStateException ex) {
                    ConsoleUtils.printError(ex.getMessage());
                }
            });
        }
        ConsoleUtils.printSuccess("Attendance recorded for " + date);
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewAttendanceForCourse(Teacher teacher) {
        String code = ConsoleUtils.prompt("Enter Course Code");
        Optional<Course> courseOpt = ctx.getCourseService().findByCourseCode(code);
        if (courseOpt.isEmpty() || !courseOpt.get().getTeacherId().equals(teacher.getId())) {
            ConsoleUtils.printError("Course not found or not assigned to you.");
            ConsoleUtils.pressEnterToContinue();
            return;
        }
        Course course = courseOpt.get();
        ConsoleUtils.printHeader("Attendance — " + course.getTitle());
        List<Attendance> records = ctx.getAttendanceService().getAttendanceByCourse(course.getId());
        if (records.isEmpty()) {
            ConsoleUtils.printInfo("No attendance records found.");
        } else {
            System.out.printf("%-12s %-25s %-12s %-10s%n", "StudentNo", "Name", "Date", "Status");
            ConsoleUtils.printSeparator();
            records.forEach(a ->
                ctx.getStudentService().findById(a.getStudentId()).ifPresent(s ->
                    System.out.printf("%-12s %-25s %-12s %-10s%n",
                        s.getStudentNumber(), s.getFullName(), a.getDate(), a.getStatus())));
        }
        ConsoleUtils.pressEnterToContinue();
    }

    private void viewCourseSchedule(Teacher teacher) {
        ConsoleUtils.printHeader("My Course Schedules");
        List<Course> courses = ctx.getCourseService().getCoursesByTeacher(teacher.getId());
        courses.forEach(c -> {
            List<Schedule> schedules = ctx.getScheduleService().getSchedulesByCourse(c.getId());
            if (!schedules.isEmpty()) {
                System.out.println("\n  " + c.getCourseCode() + " — " + c.getTitle());
                schedules.forEach(s -> System.out.printf("    %-10s  %s - %s  Room: %s%n",
                        s.getDayOfWeek(), s.getStartTime(), s.getEndTime(), s.getRoom()));
            }
        });
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
