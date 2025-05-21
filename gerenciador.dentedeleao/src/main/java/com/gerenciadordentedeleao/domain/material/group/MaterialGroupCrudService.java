package com.gerenciadordentedeleao.domain.material.group;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.UUID;

@Service
public class MaterialGroupCrudService extends AbstractCrudService<MaterialGroupEntity> {

    protected MaterialGroupCrudService(MaterialGroupRepository repository) {
        super(repository);
    }

}
