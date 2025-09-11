package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.domain.consultation.ConsultationCrudService;
import com.gerenciadordentedeleao.domain.consultation.dto.PayloadConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.ResponseConsultationDTO;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entities/consultation")
public class ConsultationController {

    private final ConsultationCrudService consultationCrudService;

    @Autowired
    protected ConsultationController(ConsultationCrudService consultationCrudService) {
        this.consultationCrudService = consultationCrudService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseConsultationDTO> finDTOById(@PathVariable UUID id) {
        var responseConsultationDTO = consultationCrudService.findById(id);
        return ResponseEntity.ok(responseConsultationDTO);
    }

    @GetMapping
    public ResponseEntity<List<ResponseConsultationDTO>> findAllDTO() {
        var responseConsultationDTOList = consultationCrudService.findAll();
        return ResponseEntity.ok(responseConsultationDTOList);
    }

    @PostMapping()
    public ResponseEntity<ResponseConsultationDTO> create(@RequestBody PayloadConsultationDTO dto) {
        ResponseConsultationDTO responseConsultationDTO = consultationCrudService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseConsultationDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseConsultationDTO> update(@PathVariable("id") UUID id, @RequestBody PayloadConsultationDTO dto) {
        ResponseConsultationDTO responseConsultationDTO = consultationCrudService.update(dto, id);
        return ResponseEntity.ok(responseConsultationDTO);
    }

    @DeleteMapping("finalizar/{id}")
    public ResponseEntity<Void> finalizarConsulta(@PathVariable("id") UUID id) {
        consultationCrudService.finalizarConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        consultationCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
