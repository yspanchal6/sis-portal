package com.sis.service;

import com.sis.model.Schedule;
import com.sis.repository.CourseRepository;
import com.sis.repository.ScheduleRepository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Business logic for managing course schedules.
 */
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository   courseRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, CourseRepository courseRepository) {
        this.scheduleRepository = scheduleRepository;
        this.courseRepository   = courseRepository;
    }

    public Schedule addSchedule(Schedule schedule) {
        // Ensure course exists
        courseRepository.findById(schedule.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + schedule.getCourseId()));
        scheduleRepository.save(schedule);
        return schedule;
    }

    public List<Schedule> getSchedulesByCourse(String courseId) {
        return scheduleRepository.findByCourseId(courseId);
    }

    public List<Schedule> getSchedulesByDay(DayOfWeek day) {
        return scheduleRepository.findByDayOfWeek(day);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public void deleteSchedule(String scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}
