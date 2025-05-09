package com.gerenciadordentedeleao.application.abstractions;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractCrudService <T extends Persistable<UUID>> {

    protected final JpaRepository<T, UUID> repository;

    protected AbstractCrudService(JpaRepository<T, UUID> repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public Optional<T> findById(UUID id) {
        return repository.findById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
