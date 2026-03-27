package com.sis.service;

import com.sis.model.Attendance;
import com.sis.model.Enrollment;
import com.sis.repository.AttendanceRepository;
import com.sis.repository.EnrollmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for managing attendance records.
 */
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Records attendance for a student in a course on the given date.
     * If a record already exists for that student/course/date, it is updated.
     */
    public Attendance markAttendance(String studentId, String courseId,
                                     LocalDate date, Attendance.Status status) {
        // Verify enrollment
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(studentId, courseId);
        if (enrollment.isEmpty() || enrollment.get().getStatus() == Enrollment.Status.WITHDRAWN) {
            throw new IllegalStateException("Student is not actively enrolled in this course.");
        }

        // Upsert
        Optional<Attendance> existing = attendanceRepository.findByStudentCourseAndDate(studentId, courseId, date);
        Attendance record;
        if (existing.isPresent()) {
            record = existing.get();
            record.setStatus(status);
        } else {
            record = new Attendance(studentId, courseId, date, status);
        }
        attendanceRepository.save(record);
        return record;
    }

    public List<Attendance> getAttendanceByStudent(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceByCourse(String courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }

    public List<Attendance> getAttendanceByStudentAndCourse(String studentId, String courseId) {
        return attendanceRepository.findByStudentAndCourse(studentId, courseId);
    }

    public List<Attendance> getAttendanceByCourseAndDate(String courseId, LocalDate date) {
        return attendanceRepository.findByCourseAndDate(courseId, date);
    }

    /**
     * Returns the attendance percentage (PRESENT + LATE) for a student in a course.
     */
    public double getAttendancePercentage(String studentId, String courseId) {
        List<Attendance> records = attendanceRepository.findByStudentAndCourse(studentId, courseId);
        if (records.isEmpty()) return 0.0;
        long attended = records.stream()
                .filter(a -> a.getStatus() != Attendance.Status.ABSENT)
                .count();
        return (attended * 100.0) / records.size();
    }
}
