package com.gerenciadordentedeleao.domain.material.dto;

import com.gerenciadordentedeleao.domain.material.historic.MovementType;

import java.util.UUID;

public record MovementStockDTO(UUID materialId, MovementType movementType, int quantity, UUID reserveId) {
}
