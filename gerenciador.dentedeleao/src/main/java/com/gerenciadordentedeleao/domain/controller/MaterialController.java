package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.dto.CreateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.dto.UpdateMaterialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entities/material")
public class MaterialController {

    private final MaterialCrudService materialCrudService;

    @Autowired
    public MaterialController(MaterialCrudService materialCrudService) {
        this.materialCrudService = materialCrudService;
    }

    @GetMapping("{id}")
    public ResponseEntity<MaterialEntity> findMaterial(@PathVariable UUID id) {
        MaterialEntity material = materialCrudService.findById(id).orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + id));
        return ResponseEntity.ok(material);
    }

    @GetMapping
    public ResponseEntity<List<MaterialEntity>> findAllMaterials() {
        return ResponseEntity.ok(materialCrudService.findAll());
    }

    @PostMapping
    public ResponseEntity<MaterialEntity> create(@RequestBody CreateMaterialDTO dto) {
        MaterialEntity novoMaterial = materialCrudService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMaterial);
    }

    @PutMapping
    public ResponseEntity<MaterialEntity> updateMaterial(@RequestBody UpdateMaterialDTO dto) {
        MaterialEntity material = materialCrudService.update(dto);
        return ResponseEntity.ok(material);
    }

    @PostMapping("/movement-stock")
    public ResponseEntity<MaterialEntity> movementStock(@RequestBody MovementStockDTO dto) {
        MaterialEntity material = materialCrudService.movementStock(dto);
        return ResponseEntity.ok(material);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMaterial(@RequestParam UUID id) {
        materialCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
