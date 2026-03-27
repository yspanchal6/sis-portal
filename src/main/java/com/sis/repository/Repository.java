package com.sis.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD repository contract.
 *
 * @param <T>  entity type
 */
public interface Repository<T> {

    void      save(T entity);
    Optional<T> findById(String id);
    List<T>   findAll();
    void      deleteById(String id);
    boolean   existsById(String id);
}
