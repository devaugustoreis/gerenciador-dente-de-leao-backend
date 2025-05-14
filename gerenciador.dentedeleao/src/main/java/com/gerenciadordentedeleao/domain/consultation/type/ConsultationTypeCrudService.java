package com.gerenciadordentedeleao.domain.consultation.type;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.UUID;

@Service
public class ConsultationTypeCrudService extends AbstractCrudService<ConsultationTypeEntity> {

    @Autowired
    private ConsultationTypeRepository consultationTypeRepository;

    protected ConsultationTypeCrudService(ConsultationTypeRepository repository) {
        super(repository);
    }

    @DeleteMapping("/{id}")
    public void logicalDeleteConsultationType(UUID id) {
        consultationTypeRepository.findById(id).map(consultationType -> {
            consultationType.setExcluded(true);
            consultationTypeRepository.save(consultationType);
            return true;
        });
    }
}
