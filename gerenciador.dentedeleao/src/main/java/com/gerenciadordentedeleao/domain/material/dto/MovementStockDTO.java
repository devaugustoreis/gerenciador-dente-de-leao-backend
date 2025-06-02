package com.gerenciadordentedeleao.domain.material.dto;

import com.gerenciadordentedeleao.domain.material.historic.MovementType;

public record MovementStockDTO(MovementType movementType, int quantity) {
}
