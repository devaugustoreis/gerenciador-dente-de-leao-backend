package com.gerenciadordentedeleao.domain.consultation.dto;

import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record UpdateConsultationDTO(
        UUID consultationId,
        String patientName,
        Timestamp startDate,
        Timestamp endDate,
        List<MaterialConsultationDTO> materials,
        UUID consultationTypeId) {
}
