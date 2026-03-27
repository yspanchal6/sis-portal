package com.sis.repository;

import com.sis.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Attendance records.
 */
public class AttendanceRepository extends InMemoryRepository<Attendance> {

    @Override
    protected String getId(Attendance entity) {
        return entity.getId();
    }

    public List<Attendance> findByStudentId(String studentId) {
        return store.values().stream()
                .filter(a -> a.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Attendance> findByCourseId(String courseId) {
        return store.values().stream()
                .filter(a -> a.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public List<Attendance> findByStudentAndCourse(String studentId, String courseId) {
        return store.values().stream()
                .filter(a -> a.getStudentId().equals(studentId) && a.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public Optional<Attendance> findByStudentCourseAndDate(String studentId, String courseId, LocalDate date) {
        return store.values().stream()
                .filter(a -> a.getStudentId().equals(studentId)
                          && a.getCourseId().equals(courseId)
                          && a.getDate().equals(date))
                .findFirst();
    }

    public List<Attendance> findByCourseAndDate(String courseId, LocalDate date) {
        return store.values().stream()
                .filter(a -> a.getCourseId().equals(courseId) && a.getDate().equals(date))
                .collect(Collectors.toList());
    }
}
