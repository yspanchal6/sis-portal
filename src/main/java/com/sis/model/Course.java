package com.sis.model;

import java.util.UUID;

/**
 * Represents an academic course offered by the institution.
 */
public class Course {

    private final String id;
    private String courseCode;      // e.g. "CS101"
    private String title;
    private String description;
    private int    credits;
    private String teacherId;       // FK to Teacher.id
    private int    capacity;
    private int    enrolledCount;
    private boolean active;

    public Course(String courseCode, String title, String description,
                  int credits, String teacherId, int capacity) {
        this.id            = UUID.randomUUID().toString();
        this.courseCode    = courseCode;
        this.title         = title;
        this.description   = description;
        this.credits       = credits;
        this.teacherId     = teacherId;
        this.capacity      = capacity;
        this.enrolledCount = 0;
        this.active        = true;
    }

    // ---------------------------------------------------------------- getters / setters

    public String getId()           { return id; }
    public String getCourseCode()   { return courseCode; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public int    getCredits()      { return credits; }
    public String getTeacherId()    { return teacherId; }
    public int    getCapacity()     { return capacity; }
    public int    getEnrolledCount(){ return enrolledCount; }
    public boolean isActive()       { return active; }

    public void setCourseCode(String courseCode)   { this.courseCode  = courseCode; }
    public void setTitle(String title)             { this.title       = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCredits(int credits)            { this.credits     = credits; }
    public void setTeacherId(String teacherId)     { this.teacherId   = teacherId; }
    public void setCapacity(int capacity)          { this.capacity    = capacity; }
    public void setEnrolledCount(int count)        { this.enrolledCount = count; }
    public void setActive(boolean active)          { this.active      = active; }

    public boolean hasSpace() {
        return enrolledCount < capacity;
    }

    @Override
    public String toString() {
        return String.format("Course{code=%s, title=%s, credits=%d, enrolled=%d/%d, active=%b}",
                courseCode, title, credits, enrolledCount, capacity, active);
    }
}
