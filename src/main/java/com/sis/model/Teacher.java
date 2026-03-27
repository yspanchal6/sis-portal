package com.sis.model;

/**
 * Represents a teacher user in the system.
 */
public class Teacher extends User {

    private String teacherNumber;   // e.g. "TCH-0001"
    private String department;
    private String phone;
    private String qualification;

    public Teacher(String username, String password, String fullName, String email,
                   String teacherNumber, String department, String phone, String qualification) {
        super(username, password, fullName, email, Role.TEACHER);
        this.teacherNumber = teacherNumber;
        this.department    = department;
        this.phone         = phone;
        this.qualification = qualification;
    }

    // ---------------------------------------------------------------- getters / setters

    public String getTeacherNumber()  { return teacherNumber; }
    public String getDepartment()     { return department; }
    public String getPhone()          { return phone; }
    public String getQualification()  { return qualification; }

    public void setTeacherNumber(String teacherNumber) { this.teacherNumber = teacherNumber; }
    public void setDepartment(String department)       { this.department    = department; }
    public void setPhone(String phone)                 { this.phone         = phone; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    @Override
    public String toString() {
        return String.format("Teacher{id=%s, teacherNumber=%s, name=%s, dept=%s}",
                getId(), teacherNumber, getFullName(), department);
    }
}
