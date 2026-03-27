package com.sis.service;

import com.sis.model.Course;
import com.sis.model.Enrollment;
import com.sis.repository.CourseRepository;
import com.sis.repository.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for student enrollment in courses.
 */
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository     courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository     = courseRepository;
    }

    /**
     * Enrolls a student in a course.
     * Validates capacity and duplicate enrollment.
     */
    public Enrollment enroll(String studentId, String courseId) {
        // Check already enrolled
        Optional<Enrollment> existing = enrollmentRepository.findByStudentAndCourse(studentId, courseId);
        if (existing.isPresent() && existing.get().getStatus() == Enrollment.Status.ENROLLED) {
            throw new IllegalStateException("Student is already enrolled in this course.");
        }

        // Check course capacity
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        if (!course.hasSpace()) {
            throw new IllegalStateException("Course '" + course.getTitle() + "' is at full capacity.");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment(studentId, courseId);
        enrollmentRepository.save(enrollment);

        // Increment enrolled count
        course.setEnrolledCount(course.getEnrolledCount() + 1);
        courseRepository.save(course);

        return enrollment;
    }

    /**
     * Withdraws a student from a course.
     */
    public void withdraw(String studentId, String courseId) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentAndCourse(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));

        enrollment.setStatus(Enrollment.Status.WITHDRAWN);
        enrollmentRepository.save(enrollment);

        // Decrement enrolled count
        courseRepository.findById(courseId).ifPresent(c -> {
            c.setEnrolledCount(Math.max(0, c.getEnrolledCount() - 1));
            courseRepository.save(c);
        });
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> findByStudentAndCourse(String studentId, String courseId) {
        return enrollmentRepository.findByStudentAndCourse(studentId, courseId);
    }
}
