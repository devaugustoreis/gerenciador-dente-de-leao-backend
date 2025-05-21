package com.gerenciadordentedeleao.application.abstractions;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.reflect.Field;
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

    public void logicalDelete(UUID id, String deleted_col_name) {
        repository.findById(id).map(entity -> {
            try {
                Field field = entity.getClass().getDeclaredField(deleted_col_name);
                field.setAccessible(true);
                field.set(entity, true);
                repository.save(entity);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Erro ao marcar como excluído: " + e.getMessage(), e);
            }
            return true;
        });
    }
}
