package com.sis.repository;

import com.sis.model.Grade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Grade entities.
 */
public class GradeRepository extends InMemoryRepository<Grade> {

    @Override
    protected String getId(Grade entity) {
        return entity.getId();
    }

    public List<Grade> findByStudentId(String studentId) {
        return store.values().stream()
                .filter(g -> g.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Grade> findByCourseId(String courseId) {
        return store.values().stream()
                .filter(g -> g.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public Optional<Grade> findByStudentAndCourse(String studentId, String courseId) {
        return store.values().stream()
                .filter(g -> g.getStudentId().equals(studentId) && g.getCourseId().equals(courseId))
                .findFirst();
    }
}
