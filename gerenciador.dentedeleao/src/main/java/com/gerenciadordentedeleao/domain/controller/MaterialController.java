package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.dto.CreateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.dto.UpdateMaterialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        MaterialEntity material = materialCrudService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));
        return ResponseEntity.ok(material);
    }

    @GetMapping
    public ResponseEntity<Page<MaterialEntity>> findAllMaterials(Pageable pageable) {
        return ResponseEntity.ok(materialCrudService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<MaterialEntity> create(@RequestBody CreateMaterialDTO dto) {
        MaterialEntity novoMaterial = materialCrudService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMaterial);
    }

    @PutMapping("{id}")
    public ResponseEntity<MaterialEntity> updateMaterial(@PathVariable UUID id, @RequestBody UpdateMaterialDTO dto) {
        MaterialEntity material = materialCrudService.update(id, dto);
        return ResponseEntity.ok(material);
    }

    @GetMapping("und_restantes/{id}")
    public ResponseEntity<Integer> findUndRestantesMaterial(@PathVariable UUID id) {
        int und_restantes = materialCrudService.findById(id).map(MaterialEntity::getStockQuantity).orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));
        return ResponseEntity.ok(und_restantes);
    }

    @GetMapping("agendado/{id}")
    public ResponseEntity<Integer> findQuantidadeAgendadaMaterial(@PathVariable UUID id) {
        int quantidadeAgendada = materialCrudService.findById(id).map(MaterialEntity::getScheduledQuantity).orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));
        return ResponseEntity.ok(quantidadeAgendada);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImagem(@PathVariable UUID id, @RequestParam("image") MultipartFile arquivo) {
        materialCrudService.uploadImage(id, arquivo);
        return ResponseEntity.ok("Imagem salva com sucesso!");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> findImage(@PathVariable UUID id) {
        MaterialEntity material = materialCrudService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));
        if (material.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(material.getImage(), headers, HttpStatus.OK);
    }

    @PostMapping("/movement-stock/{id}")
    public ResponseEntity<MaterialEntity> movementStock(@PathVariable UUID id,@RequestBody MovementStockDTO dto) {
        MaterialEntity material = materialCrudService.movementStock(id, dto);
        return ResponseEntity.ok(material);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMaterial(@RequestParam UUID id) {
        materialCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
