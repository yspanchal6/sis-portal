package com.sis.model;

/**
 * Represents an administrative user in the system.
 * Admins have full access to manage users, courses, and system data.
 */
public class Admin extends User {

    private String adminNumber;     // e.g. "ADM-0001"

    public Admin(String username, String password, String fullName,
                 String email, String adminNumber) {
        super(username, password, fullName, email, Role.ADMIN);
        this.adminNumber = adminNumber;
    }

    // ---------------------------------------------------------------- getters / setters

    public String getAdminNumber() { return adminNumber; }
    public void setAdminNumber(String adminNumber) { this.adminNumber = adminNumber; }

    @Override
    public String toString() {
        return String.format("Admin{id=%s, adminNumber=%s, name=%s}",
                getId(), adminNumber, getFullName());
    }
}
