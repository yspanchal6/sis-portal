package com.sis.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a student's enrollment in a course.
 */
public class Enrollment {

    public enum Status { ENROLLED, WITHDRAWN, COMPLETED }

    private final String id;
    private String    studentId;
    private String    courseId;
    private LocalDate enrollmentDate;
    private Status    status;

    public Enrollment(String studentId, String courseId) {
        this.id             = UUID.randomUUID().toString();
        this.studentId      = studentId;
        this.courseId       = courseId;
        this.enrollmentDate = LocalDate.now();
        this.status         = Status.ENROLLED;
    }

    // ---------------------------------------------------------------- getters / setters

    public String    getId()             { return id; }
    public String    getStudentId()      { return studentId; }
    public String    getCourseId()       { return courseId; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public Status    getStatus()         { return status; }

    public void setStudentId(String studentId)           { this.studentId      = studentId; }
    public void setCourseId(String courseId)             { this.courseId       = courseId; }
    public void setEnrollmentDate(LocalDate date)        { this.enrollmentDate = date; }
    public void setStatus(Status status)                 { this.status         = status; }

    @Override
    public String toString() {
        return String.format("Enrollment{id=%s, studentId=%s, courseId=%s, date=%s, status=%s}",
                id, studentId, courseId, enrollmentDate, status);
    }
}
