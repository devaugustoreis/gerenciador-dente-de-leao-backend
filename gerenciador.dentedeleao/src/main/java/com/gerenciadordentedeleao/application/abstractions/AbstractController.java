package com.gerenciadordentedeleao.application.abstractions;

import org.springframework.data.domain.Persistable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public abstract class AbstractController<T extends Persistable<UUID>> {

    private final AbstractCrudService<T> crudService;

    protected AbstractController(AbstractCrudService<T> crudService) {
        this.crudService = crudService;
    }

    @GetMapping("{id}")
    public ResponseEntity<T> findById(@PathVariable UUID id) {
        var entity = crudService.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
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
}
