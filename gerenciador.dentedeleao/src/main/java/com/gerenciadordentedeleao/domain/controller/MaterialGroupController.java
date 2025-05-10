package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.abstractions.AbstractController;
import com.gerenciadordentedeleao.domain.material.group.MaterialGroupCrudService;
import com.gerenciadordentedeleao.domain.material.group.MaterialGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entities/material-group")
public class MaterialGroupController extends AbstractController<MaterialGroupEntity> {

    @Autowired
    protected MaterialGroupController(MaterialGroupCrudService crudService) {
        super(crudService);
    }
}
