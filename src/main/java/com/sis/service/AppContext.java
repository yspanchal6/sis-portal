package com.sis.service;

import com.sis.repository.*;

/**
 * Holds singleton instances of all repositories.
 * Acts as a lightweight dependency-injection container.
 */
public class AppContext {

    private final UserRepository       userRepository       = new UserRepository();
    private final StudentRepository    studentRepository    = new StudentRepository();
    private final TeacherRepository    teacherRepository    = new TeacherRepository();
    private final CourseRepository     courseRepository     = new CourseRepository();
    private final GradeRepository      gradeRepository      = new GradeRepository();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
    private final ScheduleRepository   scheduleRepository   = new ScheduleRepository();
    private final AttendanceRepository attendanceRepository = new AttendanceRepository();

    // Lazy-initialised services
    private AuthService       authService;
    private StudentService    studentService;
    private TeacherService    teacherService;
    private CourseService     courseService;
    private GradeService      gradeService;
    private EnrollmentService enrollmentService;
    private ScheduleService   scheduleService;
    private AttendanceService attendanceService;

    // ---------------------------------------------------------------- repositories

    public UserRepository       getUserRepository()       { return userRepository; }
    public StudentRepository    getStudentRepository()    { return studentRepository; }
    public TeacherRepository    getTeacherRepository()    { return teacherRepository; }
    public CourseRepository     getCourseRepository()     { return courseRepository; }
    public GradeRepository      getGradeRepository()      { return gradeRepository; }
    public EnrollmentRepository getEnrollmentRepository() { return enrollmentRepository; }
    public ScheduleRepository   getScheduleRepository()   { return scheduleRepository; }
    public AttendanceRepository getAttendanceRepository() { return attendanceRepository; }

    // ---------------------------------------------------------------- services (lazy)

    public AuthService getAuthService() {
        if (authService == null) authService = new AuthService(userRepository);
        return authService;
    }

    public StudentService getStudentService() {
        if (studentService == null) studentService = new StudentService(studentRepository, userRepository);
        return studentService;
    }

    public TeacherService getTeacherService() {
        if (teacherService == null) teacherService = new TeacherService(teacherRepository, userRepository);
        return teacherService;
    }

    public CourseService getCourseService() {
        if (courseService == null) courseService = new CourseService(courseRepository);
        return courseService;
    }

    public GradeService getGradeService() {
        if (gradeService == null) gradeService = new GradeService(gradeRepository, enrollmentRepository);
        return gradeService;
    }

    public EnrollmentService getEnrollmentService() {
        if (enrollmentService == null)
            enrollmentService = new EnrollmentService(enrollmentRepository, courseRepository);
        return enrollmentService;
    }

    public ScheduleService getScheduleService() {
        if (scheduleService == null) scheduleService = new ScheduleService(scheduleRepository, courseRepository);
        return scheduleService;
    }

    public AttendanceService getAttendanceService() {
        if (attendanceService == null)
            attendanceService = new AttendanceService(attendanceRepository, enrollmentRepository);
        return attendanceService;
    }
}
