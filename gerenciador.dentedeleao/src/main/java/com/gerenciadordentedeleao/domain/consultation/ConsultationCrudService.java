package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConsultationCrudService extends AbstractCrudService<ConsultationEntity> {

    @Autowired
    private ConsultationRepository consultationRepository;

    public ConsultationCrudService(ConsultationRepository consultationRepository) {
        super(consultationRepository);
    }

    public void logicalDeleteConsultation(UUID id) {
        consultationRepository.findById(id).ifPresent(consultation -> {
            consultation.setConcluded(true);
            consultationRepository.save(consultation);
        });
    }
}
