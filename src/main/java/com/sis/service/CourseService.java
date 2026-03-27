package com.sis.service;

import com.sis.model.Course;
import com.sis.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for managing Course records.
 */
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course addCourse(Course course) {
        if (courseRepository.findByCourseCode(course.getCourseCode()).isPresent()) {
            throw new IllegalArgumentException("Course code '" + course.getCourseCode() + "' already exists.");
        }
        courseRepository.save(course);
        return course;
    }

    public Optional<Course> findById(String id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> findByCourseCode(String code) {
        return courseRepository.findByCourseCode(code);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findActive();
    }

    public List<Course> getCoursesByTeacher(String teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public Course updateCourse(Course course) {
        if (!courseRepository.existsById(course.getId())) {
            throw new IllegalArgumentException("Course not found: " + course.getId());
        }
        courseRepository.save(course);
        return course;
    }

    public void deactivateCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        course.setActive(false);
        courseRepository.save(course);
    }
}
