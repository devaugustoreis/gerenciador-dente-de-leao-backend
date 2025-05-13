package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.abstractions.AbstractController;
import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeCrudService;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.dto.MaterialRequestDTO;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entities/materials")
public class MaterialController extends AbstractController<MaterialEntity> {

    @Autowired
    private MaterialCrudService materialCrudService;

    @Autowired
    protected MaterialController(MaterialCrudService crudService) {
        super(crudService);
    }

    @PostMapping
    public ResponseEntity<MaterialEntity> criarMaterial(@RequestBody MaterialRequestDTO dto) {
        MaterialEntity novoMaterial = materialCrudService.createMaterial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMaterial);
    }

    // GET, PUT, DELETE etc.
}
