package com.sis.model;

import java.util.UUID;

/**
 * Represents a grade entry for a student in a specific course.
 */
public class Grade {

    private final String id;
    private String studentId;       // FK to Student.id
    private String courseId;        // FK to Course.id
    private double marks;           // 0 – 100
    private String semester;        // e.g. "Fall"
    private int    academicYear;    // e.g. 2025

    public Grade(String studentId, String courseId, double marks,
                 String semester, int academicYear) {
        this.id           = UUID.randomUUID().toString();
        this.studentId    = studentId;
        this.courseId     = courseId;
        this.marks        = marks;
        this.semester     = semester;
        this.academicYear = academicYear;
    }

    // ---------------------------------------------------------------- getters / setters

    public String getId()           { return id; }
    public String getStudentId()    { return studentId; }
    public String getCourseId()     { return courseId; }
    public double getMarks()        { return marks; }
    public String getSemester()     { return semester; }
    public int    getAcademicYear() { return academicYear; }

    public void setStudentId(String studentId)     { this.studentId    = studentId; }
    public void setCourseId(String courseId)       { this.courseId     = courseId; }
    public void setMarks(double marks)             { this.marks        = marks; }
    public void setSemester(String semester)       { this.semester     = semester; }
    public void setAcademicYear(int academicYear)  { this.academicYear = academicYear; }

    /**
     * Computes the letter grade based on marks.
     */
    public String getLetterGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }

    /**
     * Returns grade points on a 4.0 scale.
     */
    public double getGradePoints() {
        if (marks >= 90) return 4.0;
        if (marks >= 80) return 3.7;
        if (marks >= 70) return 3.0;
        if (marks >= 60) return 2.0;
        if (marks >= 50) return 1.0;
        return 0.0;
    }

    @Override
    public String toString() {
        return String.format("Grade{studentId=%s, courseId=%s, marks=%.2f, letter=%s, semester=%s %d}",
                studentId, courseId, marks, getLetterGrade(), semester, academicYear);
    }
}
