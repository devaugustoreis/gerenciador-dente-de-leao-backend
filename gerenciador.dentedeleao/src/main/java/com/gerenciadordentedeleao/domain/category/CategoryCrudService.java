package com.gerenciadordentedeleao.domain.category;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryCrudService extends AbstractCrudService<CategoryEntity> {

    @Autowired
    public CategoryCrudService(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

}
