package com.sis.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Represents the weekly schedule for a course (room, day, time).
 */
public class Schedule {

    private final String id;
    private String    courseId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String    room;

    public Schedule(String courseId, DayOfWeek dayOfWeek,
                    LocalTime startTime, LocalTime endTime, String room) {
        this.id        = UUID.randomUUID().toString();
        this.courseId  = courseId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime   = endTime;
        this.room      = room;
    }

    // ---------------------------------------------------------------- getters / setters

    public String    getId()        { return id; }
    public String    getCourseId()  { return courseId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime()   { return endTime; }
    public String    getRoom()      { return room; }

    public void setCourseId(String courseId)    { this.courseId  = courseId; }
    public void setDayOfWeek(DayOfWeek day)     { this.dayOfWeek = day; }
    public void setStartTime(LocalTime start)   { this.startTime = start; }
    public void setEndTime(LocalTime end)       { this.endTime   = end; }
    public void setRoom(String room)            { this.room      = room; }

    @Override
    public String toString() {
        return String.format("Schedule{courseId=%s, day=%s, time=%s-%s, room=%s}",
                courseId, dayOfWeek, startTime, endTime, room);
    }
}
