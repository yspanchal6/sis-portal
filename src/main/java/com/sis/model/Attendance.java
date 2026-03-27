package com.sis.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an attendance record for a student in a course on a specific date.
 */
public class Attendance {

    public enum Status { PRESENT, ABSENT, LATE }

    private final String id;
    private String    studentId;
    private String    courseId;
    private LocalDate date;
    private Status    status;
    private String    remarks;

    public Attendance(String studentId, String courseId, LocalDate date, Status status) {
        this.id        = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.courseId  = courseId;
        this.date      = date;
        this.status    = status;
        this.remarks   = "";
    }

    // ---------------------------------------------------------------- getters / setters

    public String    getId()        { return id; }
    public String    getStudentId() { return studentId; }
    public String    getCourseId()  { return courseId; }
    public LocalDate getDate()      { return date; }
    public Status    getStatus()    { return status; }
    public String    getRemarks()   { return remarks; }

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setCourseId(String courseId)   { this.courseId  = courseId; }
    public void setDate(LocalDate date)        { this.date      = date; }
    public void setStatus(Status status)       { this.status    = status; }
    public void setRemarks(String remarks)     { this.remarks   = remarks; }

    @Override
    public String toString() {
        return String.format("Attendance{studentId=%s, courseId=%s, date=%s, status=%s}",
                studentId, courseId, date, status);
    }
}
