package com.gerenciadordentedeleao.application.abstractions;

import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public abstract class AbstractController<T extends PersistableEntity> {

    private final AbstractCrudService<T> crudService;

    protected AbstractController(AbstractCrudService<T> crudService) {
        this.crudService = crudService;
    }

    @GetMapping("{id}")
    public ResponseEntity<T> findById(@PathVariable UUID id) {
        var entity = crudService.findById(id).orElseThrow(() -> new ResourceNotFoundException(crudService.getEntityName(), "ID", id));
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        var entities = crudService.findAll();
        return ResponseEntity.ok(entities);
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        var createdEntity = crudService.save(entity);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEntity.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdEntity);
    }

    @PutMapping
    public ResponseEntity<T> update(@RequestBody T entity) {
        var updatedEntity = crudService.save(entity);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        crudService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
