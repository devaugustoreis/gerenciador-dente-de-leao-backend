package com.gerenciadordentedeleao.application.abstractions;

import com.gerenciadordentedeleao.application.errorhandler.BusinessException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractCrudService<T extends PersistableEntity> {

    protected final JpaRepository<T, UUID> repository;

    protected AbstractCrudService(JpaRepository<T, UUID> repository) {
        this.repository = repository;
    }

    public abstract  String getEntityName();

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
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            if (!(e.getCause() instanceof ConstraintViolationException cve && "23503".equals(cve.getSQLState()))) {
                throw new BusinessException("Erro ao excluir o registro: " + e.getMessage(), e);
            }
            var entity = repository.findById(id).orElseThrow(() -> new BusinessException("%s não econtrado com o ID: %s para realizar a exclusão".formatted(getEntityName(), id)));
            var markedAsDeleted = entity.setAsDeleted();
            if (markedAsDeleted) {
                repository.save(entity);
            }
        } catch (Exception e) {
            throw new BusinessException("Erro ao excluir o registro: " + e.getMessage(), e);
        }
    }
}
