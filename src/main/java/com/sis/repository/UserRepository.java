package com.sis.repository;

import com.sis.model.User;

import java.util.Optional;

/**
 * Repository for all User subtypes (Admin, Teacher, Student).
 */
public class UserRepository extends InMemoryRepository<User> {

    @Override
    protected String getId(User entity) {
        return entity.getId();
    }

    /** Find a user by their login username. */
    public Optional<User> findByUsername(String username) {
        return store.values().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }
}
