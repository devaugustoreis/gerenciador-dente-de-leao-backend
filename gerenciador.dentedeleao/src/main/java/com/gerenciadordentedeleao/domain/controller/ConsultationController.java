package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.abstractions.AbstractController;
import com.gerenciadordentedeleao.domain.consultation.ConsultationCrudService;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import com.gerenciadordentedeleao.domain.consultation.dto.PlayloadConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.ResponseConsultationDTO;
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
    public ResponseEntity<ResponseConsultationDTO> create(@RequestBody PlayloadConsultationDTO dto){
        ResponseConsultationDTO responseConsultationDTO = consultationCrudService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseConsultationDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseConsultationDTO> update(@PathVariable("id") UUID id, @RequestBody PlayloadConsultationDTO dto) {
        ResponseConsultationDTO responseConsultationDTO = consultationCrudService.update(dto, id);
        return ResponseEntity.ok(responseConsultationDTO);
    }

    @DeleteMapping("/finalizar/{id}")
    public ResponseEntity<Void> finalizarConsulta( @PathVariable("id") UUID id) {
        consultationCrudService.finalizarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
