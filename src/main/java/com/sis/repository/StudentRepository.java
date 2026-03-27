package com.sis.repository;

import com.sis.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Student entities with additional query methods.
 */
public class StudentRepository extends InMemoryRepository<Student> {

    @Override
    protected String getId(Student entity) {
        return entity.getId();
    }

    public Optional<Student> findByStudentNumber(String studentNumber) {
        return store.values().stream()
                .filter(s -> s.getStudentNumber().equalsIgnoreCase(studentNumber))
                .findFirst();
    }

    public List<Student> findByMajor(String major) {
        return store.values().stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
    }

    public List<Student> findByEnrollmentYear(int year) {
        return store.values().stream()
                .filter(s -> s.getEnrollmentYear() == year)
                .collect(Collectors.toList());
    }
}
