package com.gerenciadordentedeleao.domain.material.dto;

import java.util.UUID;

public record UpdateMaterialDTO(UUID materialId, String name, UUID categoryId) {
}
