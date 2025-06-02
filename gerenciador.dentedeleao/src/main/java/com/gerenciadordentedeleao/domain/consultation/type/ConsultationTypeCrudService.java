package com.gerenciadordentedeleao.domain.consultation.type;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class ConsultationTypeCrudService extends AbstractCrudService<ConsultationTypeEntity> {

    protected ConsultationTypeCrudService(ConsultationTypeRepository repository) {
        super(repository);
    }
}
