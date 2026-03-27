package com.sis.model;

import java.util.UUID;

/**
 * Abstract base class representing an authenticated user of the system.
 */
public abstract class User {

    private final String id;
    private String username;
    private String password;      // stored as plain-text for simplicity; hash in production
    private String fullName;
    private String email;
    private final Role role;
    private boolean active;

    protected User(String username, String password, String fullName, String email, Role role) {
        this.id       = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email    = email;
        this.role     = role;
        this.active   = true;
    }

    // ---------------------------------------------------------------- getters

    public String getId()       { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getEmail()    { return email; }
    public Role   getRole()     { return role; }
    public boolean isActive()   { return active; }

    // ---------------------------------------------------------------- setters

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email)       { this.email    = email; }
    public void setActive(boolean active)    { this.active   = active; }

    @Override
    public String toString() {
        return String.format("[%s] id=%s, username=%s, name=%s, email=%s, active=%b",
                role, id, username, fullName, email, active);
    }
}
