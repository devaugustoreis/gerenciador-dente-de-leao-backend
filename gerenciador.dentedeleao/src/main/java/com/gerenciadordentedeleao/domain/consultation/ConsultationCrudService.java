package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.consultation.dto.ConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsCrudService;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsEntity;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsRepository;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeRepository;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultationCrudService extends AbstractCrudService<ConsultationEntity> {

    private final ConsultationMaterialsCrudService consultationMaterialsCrudService;

    private final ConsultationMaterialsRepository consultationMaterialRepository;

    private final ConsultationTypeRepository consultationTypeRepository;

    private final MaterialRepository materialRepository;

    private final ConsultationRepository consultationRepository;

    public ConsultationCrudService(ConsultationRepository consultationRepository, ConsultationMaterialsCrudService consultationMaterialsCrudService, ConsultationMaterialsRepository consultationMaterialRepository, ConsultationTypeRepository consultationTypeRepository, MaterialRepository materialRepository) {
        super(consultationRepository);
        this.consultationMaterialsCrudService = consultationMaterialsCrudService;
        this.consultationMaterialRepository = consultationMaterialRepository;
        this.consultationTypeRepository = consultationTypeRepository;
        this.materialRepository = materialRepository;
        this.consultationRepository = consultationRepository;
    }

    public ConsultationEntity create(ConsultationDTO dto) {
        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta", "ID", dto.consultationTypeId()));

        ConsultationEntity consultation = new ConsultationEntity();

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());
        consultation.setConsultationType(consultationType);

        consultation = repository.save(consultation);

        consultationMaterialsCrudService.createConsultationMaterials(dto, consultation);

        return consultation;
    }


    public ConsultationEntity update(ConsultationDTO dto, UUID id) {
        ConsultationEntity consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());

        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta", "ID", dto.consultationTypeId()));
        consultation.setConsultationType(consultationType);

        consultation = repository.save(consultation);

        consultationMaterialsCrudService.updateConsultationMaterials(dto, consultation);

        return consultation;
    }

    public void finalizarConsulta(UUID id) {

        ConsultationEntity consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));
        consultation.setConcluded(true);

        consultationRepository.save(consultation);

        List<ConsultationMaterialsEntity> consultationMaterialEntity = consultationMaterialRepository.findByIdReturnId(id);

        for (ConsultationMaterialsEntity entity : consultationMaterialEntity) {
            MaterialEntity material = materialRepository.findById(entity.getMaterial().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", entity.getMaterial().getId()));

            consultationMaterialsCrudService.getTotalFutureMaterialQuantity(material.getId(), material);
        }
    }

    @Override
    public String getEntityName() {
        return "Consulta";
    }
}
