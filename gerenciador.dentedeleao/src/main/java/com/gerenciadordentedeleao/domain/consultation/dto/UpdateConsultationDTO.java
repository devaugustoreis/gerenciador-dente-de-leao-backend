package com.gerenciadordentedeleao.domain.consultation.dto;

import com.gerenciadordentedeleao.domain.material.dto.MaterialItemDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record UpdateConsultationDTO(
        UUID consultation_id,
        String patient_name,
        Timestamp start_date,
        Timestamp end_date,
        List<MaterialItemDTO> materials,
        UUID consultation_type_id) {
}
