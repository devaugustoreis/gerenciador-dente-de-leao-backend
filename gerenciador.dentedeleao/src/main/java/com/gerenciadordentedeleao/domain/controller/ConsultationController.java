package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.domain.consultation.ConsultationCrudService;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import com.gerenciadordentedeleao.domain.consultation.dto.PayloadConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.ResponseConsultationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<ResponseConsultationDTO>> findAllDTO(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                    Pageable pageable) {
        var responseConsultationDTOList = consultationCrudService.findAll(startDate, endDate, pageable);
        return ResponseEntity.ok(responseConsultationDTOList);
    }

    @GetMapping("/concluded-false")
    public ResponseEntity<Page<ResponseConsultationDTO>> findByConcludedFalse(Pageable pageable) {
        return ResponseEntity.ok(consultationCrudService.findByStatusScheduled(pageable));
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

    @PostMapping("finalizar/{id}")
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
