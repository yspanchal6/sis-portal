package com.sis.service;

import com.sis.model.Enrollment;
import com.sis.model.Grade;
import com.sis.repository.EnrollmentRepository;
import com.sis.repository.GradeRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for managing student grades.
 */
public class GradeService {

    private final GradeRepository      gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeService(GradeRepository gradeRepository, EnrollmentRepository enrollmentRepository) {
        this.gradeRepository      = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Assigns or updates a grade for a student in a course.
     * The student must be enrolled (not withdrawn).
     */
    public Grade assignGrade(String studentId, String courseId, double marks,
                             String semester, int academicYear) {
        // Verify enrollment
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(studentId, courseId);
        if (enrollment.isEmpty() || enrollment.get().getStatus() == Enrollment.Status.WITHDRAWN) {
            throw new IllegalStateException("Student is not actively enrolled in this course.");
        }

        // Upsert: update if exists, create if not
        Optional<Grade> existing = gradeRepository.findByStudentAndCourse(studentId, courseId);
        Grade grade;
        if (existing.isPresent()) {
            grade = existing.get();
            grade.setMarks(marks);
            grade.setSemester(semester);
            grade.setAcademicYear(academicYear);
        } else {
            grade = new Grade(studentId, courseId, marks, semester, academicYear);
        }
        gradeRepository.save(grade);
        return grade;
    }

    public List<Grade> getGradesByStudent(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByCourse(String courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    public Optional<Grade> getGrade(String studentId, String courseId) {
        return gradeRepository.findByStudentAndCourse(studentId, courseId);
    }

    /**
     * Calculates the cumulative GPA for a student across all their graded courses.
     */
    public double calculateGpa(String studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) return 0.0;
        double totalPoints = grades.stream().mapToDouble(Grade::getGradePoints).sum();
        return totalPoints / grades.size();
    }
}
