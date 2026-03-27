package com.sis.repository;

import com.sis.model.Course;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Course entities.
 */
public class CourseRepository extends InMemoryRepository<Course> {

    @Override
    protected String getId(Course entity) {
        return entity.getId();
    }

    public Optional<Course> findByCourseCode(String courseCode) {
        return store.values().stream()
                .filter(c -> c.getCourseCode().equalsIgnoreCase(courseCode))
                .findFirst();
    }

    public List<Course> findByTeacherId(String teacherId) {
        return store.values().stream()
                .filter(c -> c.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    public List<Course> findActive() {
        return store.values().stream()
                .filter(Course::isActive)
                .collect(Collectors.toList());
    }
}
