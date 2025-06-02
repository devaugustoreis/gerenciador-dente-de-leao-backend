package com.gerenciadordentedeleao.domain.material.dto;

import java.util.UUID;

public record MaterialItemDTO(
        UUID materialId,
        Integer quantity) {
}
