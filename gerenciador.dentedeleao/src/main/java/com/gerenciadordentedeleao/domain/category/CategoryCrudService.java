package com.gerenciadordentedeleao.domain.category;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class CategoryCrudService extends AbstractCrudService<CategoryEntity> {

    public CategoryCrudService(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

    @Override
    public String getEntityName() {
        return "Categoria";
    }
}
