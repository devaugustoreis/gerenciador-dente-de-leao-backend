package com.gerenciadordentedeleao.domain.dto;

import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class MaterialRequestDTO {

    @Getter
    @Setter
    private String name;

    @Setter
    @Getter
    private Integer stockQuantity;

    @Setter
    @Getter
    private CategoryEntity categoryId;

    public String getName() {
        return name;
    }

    public CategoryEntity getCategoryId() {
        return categoryId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }
}

