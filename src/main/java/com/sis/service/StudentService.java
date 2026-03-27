package com.sis.service;

import com.sis.model.Student;
import com.sis.repository.StudentRepository;
import com.sis.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for managing Student records.
 */
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository    userRepository;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository    = userRepository;
    }

    /**
     * Registers a new student and also saves them to the shared user store
     * so they can log in via {@link AuthService}.
     */
    public Student addStudent(Student student) {
        if (userRepository.findByUsername(student.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + student.getUsername() + "' is already taken.");
        }
        studentRepository.save(student);
        userRepository.save(student);
        return student;
    }

    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    public Student updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new IllegalArgumentException("Student not found: " + student.getId());
        }
        studentRepository.save(student);
        userRepository.save(student);
        return student;
    }

    public void deactivateStudent(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        student.setActive(false);
        studentRepository.save(student);
        userRepository.save(student);
    }
}
