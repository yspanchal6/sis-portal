package com.sis.model;

import java.time.LocalDate;

/**
 * Represents a student user in the system.
 */
public class Student extends User {

    private String studentNumber;   // e.g. "STU-0001"
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String major;
    private int    enrollmentYear;

    public Student(String username, String password, String fullName, String email,
                   String studentNumber, LocalDate dateOfBirth, String address,
                   String phone, String major, int enrollmentYear) {
        super(username, password, fullName, email, Role.STUDENT);
        this.studentNumber  = studentNumber;
        this.dateOfBirth    = dateOfBirth;
        this.address        = address;
        this.phone          = phone;
        this.major          = major;
        this.enrollmentYear = enrollmentYear;
    }

    // ---------------------------------------------------------------- getters / setters

    public String      getStudentNumber()  { return studentNumber; }
    public LocalDate   getDateOfBirth()    { return dateOfBirth; }
    public String      getAddress()        { return address; }
    public String      getPhone()          { return phone; }
    public String      getMajor()          { return major; }
    public int         getEnrollmentYear() { return enrollmentYear; }

    public void setStudentNumber(String studentNumber)   { this.studentNumber  = studentNumber; }
    public void setDateOfBirth(LocalDate dateOfBirth)    { this.dateOfBirth    = dateOfBirth; }
    public void setAddress(String address)               { this.address        = address; }
    public void setPhone(String phone)                   { this.phone          = phone; }
    public void setMajor(String major)                   { this.major          = major; }
    public void setEnrollmentYear(int enrollmentYear)    { this.enrollmentYear = enrollmentYear; }

    @Override
    public String toString() {
        return String.format("Student{id=%s, studentNumber=%s, name=%s, major=%s, year=%d}",
                getId(), studentNumber, getFullName(), major, enrollmentYear);
    }
}
