package com.sis.repository;

import com.sis.model.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Enrollment entities.
 */
public class EnrollmentRepository extends InMemoryRepository<Enrollment> {

    @Override
    protected String getId(Enrollment entity) {
        return entity.getId();
    }

    public List<Enrollment> findByStudentId(String studentId) {
        return store.values().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Enrollment> findByCourseId(String courseId) {
        return store.values().stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public Optional<Enrollment> findByStudentAndCourse(String studentId, String courseId) {
        return store.values().stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getCourseId().equals(courseId))
                .findFirst();
    }

    public List<Enrollment> findByStatus(Enrollment.Status status) {
        return store.values().stream()
                .filter(e -> e.getStatus() == status)
                .collect(Collectors.toList());
    }
}
