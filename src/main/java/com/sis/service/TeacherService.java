package com.sis.service;

import com.sis.model.Teacher;
import com.sis.repository.TeacherRepository;
import com.sis.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for managing Teacher records.
 */
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository    userRepository;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository    = userRepository;
    }

    public Teacher addTeacher(Teacher teacher) {
        if (userRepository.findByUsername(teacher.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + teacher.getUsername() + "' is already taken.");
        }
        teacherRepository.save(teacher);
        userRepository.save(teacher);
        return teacher;
    }

    public Optional<Teacher> findById(String id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> findByTeacherNumber(String teacherNumber) {
        return teacherRepository.findByTeacherNumber(teacherNumber);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public List<Teacher> getTeachersByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

    public Teacher updateTeacher(Teacher teacher) {
        if (!teacherRepository.existsById(teacher.getId())) {
            throw new IllegalArgumentException("Teacher not found: " + teacher.getId());
        }
        teacherRepository.save(teacher);
        userRepository.save(teacher);
        return teacher;
    }

    public void deactivateTeacher(String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));
        teacher.setActive(false);
        teacherRepository.save(teacher);
        userRepository.save(teacher);
    }
}
