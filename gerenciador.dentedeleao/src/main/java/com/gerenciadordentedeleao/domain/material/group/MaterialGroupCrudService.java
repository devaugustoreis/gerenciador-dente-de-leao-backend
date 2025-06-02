package com.gerenciadordentedeleao.domain.material.group;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class MaterialGroupCrudService extends AbstractCrudService<MaterialGroupEntity> {

    protected MaterialGroupCrudService(MaterialGroupRepository repository) {
        super(repository);
    }

    @Override
    public String getEntityName() {
        return "Conjunto de materiais";
    }
}
