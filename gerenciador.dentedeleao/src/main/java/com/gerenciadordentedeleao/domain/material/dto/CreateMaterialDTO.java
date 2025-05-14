package com.gerenciadordentedeleao.domain.material.dto;

import java.util.UUID;

public record CreateMaterialDTO(
        String name,
        UUID categoryId) {
}

