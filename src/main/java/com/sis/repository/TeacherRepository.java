package com.sis.repository;

import com.sis.model.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Teacher entities.
 */
public class TeacherRepository extends InMemoryRepository<Teacher> {

    @Override
    protected String getId(Teacher entity) {
        return entity.getId();
    }

    public Optional<Teacher> findByTeacherNumber(String teacherNumber) {
        return store.values().stream()
                .filter(t -> t.getTeacherNumber().equalsIgnoreCase(teacherNumber))
                .findFirst();
    }

    public List<Teacher> findByDepartment(String department) {
        return store.values().stream()
                .filter(t -> t.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
}
