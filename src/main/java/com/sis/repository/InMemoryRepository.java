package com.sis.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract in-memory implementation of {@link Repository}.
 * Subclasses supply the ID extraction logic via {@link #getId(Object)}.
 *
 * @param <T> entity type
 */
public abstract class InMemoryRepository<T> implements Repository<T> {

    protected final Map<String, T> store = new LinkedHashMap<>();

    /** Extract the unique id string from the given entity. */
    protected abstract String getId(T entity);

    @Override
    public void save(T entity) {
        store.put(getId(entity), entity);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return store.containsKey(id);
    }
}
