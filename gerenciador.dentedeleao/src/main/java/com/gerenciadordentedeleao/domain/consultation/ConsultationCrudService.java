package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultationCrudService extends AbstractCrudService<ConsultationEntity> {

    @Autowired
    protected ConsultationCrudService(ConsultationRepository consultationRepository) {
        super(consultationRepository);
    }
}
