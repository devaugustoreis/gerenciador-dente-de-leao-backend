package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.abstractions.AbstractController;
import com.gerenciadordentedeleao.domain.consultation.ConsultationCrudService;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entities/consultation")
public class ConsultationController extends AbstractController<ConsultationEntity> {

    @Autowired
    protected ConsultationController(ConsultationCrudService crudService) {
        super(crudService);
    }
}
