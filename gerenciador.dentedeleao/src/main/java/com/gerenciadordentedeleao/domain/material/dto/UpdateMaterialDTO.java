package com.gerenciadordentedeleao.domain.material.dto;

import java.util.UUID;

public record UpdateMaterialDTO(String name, UUID categoryId) {
}
