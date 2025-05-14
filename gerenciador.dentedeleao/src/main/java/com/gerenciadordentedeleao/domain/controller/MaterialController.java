package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.domain.dto.MaterialRequestDTO;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/entities/materials")
public class MaterialController {

    @Autowired
    private MaterialCrudService materialCrudService;

    @PostMapping
    public ResponseEntity<MaterialEntity> criateMaterial(@RequestBody MaterialRequestDTO dto) {
        MaterialEntity novoMaterial = materialCrudService.createMaterial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMaterial);
    }

    @PutMapping("{id}")
    public ResponseEntity<MaterialEntity> updateMaterial(@RequestBody MaterialRequestDTO dto, UUID id) {
        MaterialEntity material = materialCrudService.updateMaterial(id, dto);
        return ResponseEntity.ok(material);
    }

    @GetMapping("{id}")
    public ResponseEntity<MaterialEntity> findMaterial(@PathVariable UUID id) {
        MaterialEntity material = materialCrudService.findById(id).orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + id));
        return ResponseEntity.ok(material);
    }

//    @GetMapping
//    public ResponseEntity<MaterialEntity> findAllMaterials() {
//        return ResponseEntity.ok(materialCrudService.findAll());
//    }
}
