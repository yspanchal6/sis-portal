package com.sis.service;

import com.sis.model.User;
import com.sis.repository.UserRepository;

import java.util.Optional;

/**
 * Handles authentication and session management.
 */
public class AuthService {

    private final UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Attempts to log in with the given credentials.
     *
     * @return the authenticated User, or empty if credentials are invalid/user inactive
     */
    public Optional<User> login(String username, String password) {
        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent()) {
            User user = opt.get();
            if (user.isActive() && user.getPassword().equals(password)) {
                currentUser = user;
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /** Clears the current session. */
    public void logout() {
        currentUser = null;
    }

    /** Returns the currently logged-in user, or null if no session is active. */
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Changes the password of a user.
     *
     * @param userId      ID of the user whose password is being changed
     * @param oldPassword must match the stored password
     * @param newPassword the new password to set
     * @return true if the change was successful
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
