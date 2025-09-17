package com.gerenciadordentedeleao.domain.consultation.dto;

import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ResponseConsultationDTO(
        String patientName,
        LocalDateTime startDate,
        LocalDateTime  endDate,
        List<MaterialConsultationDTO> materials,
        UUID consultationTypeId,
        UUID consultationId,
        Boolean concluded
){
}
