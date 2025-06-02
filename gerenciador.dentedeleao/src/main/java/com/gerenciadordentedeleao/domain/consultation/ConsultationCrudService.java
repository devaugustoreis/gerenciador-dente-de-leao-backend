package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class ConsultationCrudService extends AbstractCrudService<ConsultationEntity> {

    public ConsultationCrudService(ConsultationRepository consultationRepository) {
        super(consultationRepository);
    }

    @Override
    public String getEntityName() {
        return "Consulta";
    }
}
