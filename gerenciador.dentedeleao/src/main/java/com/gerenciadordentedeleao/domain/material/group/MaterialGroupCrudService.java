package com.gerenciadordentedeleao.domain.material.group;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialGroupCrudService extends AbstractCrudService<MaterialGroupEntity> {

    @Autowired
    protected MaterialGroupCrudService(MaterialGroupRepository repository) {
        super(repository);
    }
}
