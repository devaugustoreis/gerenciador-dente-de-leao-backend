package com.gerenciadordentedeleao.domain.dto;

import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MaterialRequestDTO {

    private String name;

    private Integer stockQuantity;

    private CategoryEntity categoryId;
}

