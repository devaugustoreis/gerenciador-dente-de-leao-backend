package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.abstractions.AbstractController;
import com.gerenciadordentedeleao.domain.consultation.ConsultationCrudService;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import com.gerenciadordentedeleao.domain.consultation.dto.CreateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.UpdateConsultationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/entities/consultation")
public class ConsultationController extends AbstractController<ConsultationEntity> {

    private final ConsultationCrudService consultationCrudService;

    @Autowired
    protected ConsultationController(ConsultationCrudService consultationCrudService) {
        super(consultationCrudService);
        this.consultationCrudService = consultationCrudService;
    }

    @PostMapping("/create")
    public ResponseEntity<ConsultationEntity> create(@RequestBody CreateConsultationDTO dto){
        ConsultationEntity newConsultation = consultationCrudService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newConsultation);
    }

    @PutMapping("/update")
    public ResponseEntity<ConsultationEntity> update(@RequestBody UpdateConsultationDTO dto) {
        ConsultationEntity updatedConsultation = consultationCrudService.update(dto);
        return ResponseEntity.ok(updatedConsultation);
    }

    @DeleteMapping("/finalizar/{id}")
    public ResponseEntity<Void> finalizarConsulta( @PathVariable("id") UUID id) {
        consultationCrudService.finalizarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
