package com.gerenciadordentedeleao.domain.controller;

import com.gerenciadordentedeleao.application.abstractions.AbstractController;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeCrudService;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entities/consultation-type")
public class ConsultationTypeController extends AbstractController<ConsultationTypeEntity> {

    @Autowired
    protected ConsultationTypeController(ConsultationTypeCrudService crudService) {
        super(crudService);
    }
}
