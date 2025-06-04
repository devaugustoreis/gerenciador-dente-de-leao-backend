package com.gerenciadordentedeleao.domain.material.dto;

import java.util.UUID;

public record MaterialConsultationDTO(
        UUID materialId,
        Integer quantity) {
}
