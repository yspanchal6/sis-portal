package com.sis.repository;

import com.sis.model.Schedule;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for Schedule entities.
 */
public class ScheduleRepository extends InMemoryRepository<Schedule> {

    @Override
    protected String getId(Schedule entity) {
        return entity.getId();
    }

    public List<Schedule> findByCourseId(String courseId) {
        return store.values().stream()
                .filter(s -> s.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public List<Schedule> findByDayOfWeek(DayOfWeek day) {
        return store.values().stream()
                .filter(s -> s.getDayOfWeek() == day)
                .collect(Collectors.toList());
    }

    public List<Schedule> findByRoom(String room) {
        return store.values().stream()
                .filter(s -> s.getRoom().equalsIgnoreCase(room))
                .collect(Collectors.toList());
    }
}
