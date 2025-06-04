package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.consultation.dto.CreateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.UpdateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialEntity;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialRepository;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsId;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeCrudService;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeRepository;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultationCrudService extends AbstractCrudService<ConsultationEntity> {

    @Autowired
    private MaterialCrudService materialCrudService;

    private final ConsultationMaterialRepository consultationMaterialRepository;

    private final ConsultationTypeRepository consultationTypeRepository;

    private final MaterialRepository materialRepository;

    private final ConsultationRepository consultationRepository;

    public ConsultationCrudService(ConsultationRepository consultationRepository, ConsultationMaterialRepository consultationMaterialRepository, ConsultationTypeRepository consultationTypeRepository, MaterialRepository materialRepository, ConsultationRepository consultationRepository1) {
        super(consultationRepository);
        this.consultationMaterialRepository = consultationMaterialRepository;
        this.consultationTypeRepository = consultationTypeRepository;
        this.materialRepository = materialRepository;
        this.consultationRepository = consultationRepository1;
    }

    public ConsultationEntity create(CreateConsultationDTO dto) {
        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de consulta não encontrado."));

        ConsultationEntity consultation = new ConsultationEntity();

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());
        consultation.setConsultationType(consultationType);

        consultation = repository.save(consultation);

        createConsultationMaterials(dto, consultation);

        return consultation;
    }

    private void createConsultationMaterials(CreateConsultationDTO dto, ConsultationEntity consultation) {
        for (MaterialConsultationDTO materialDTO : dto.materials()) {
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + materialDTO.materialId()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialEntity consultationMaterialEntity = new ConsultationMaterialEntity();
            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setConsultation(consultation);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.materialId(), material);
        }
    }


    public ConsultationEntity update(UpdateConsultationDTO dto) {
        ConsultationEntity consultation = consultationRepository.findById(dto.consultationId())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com o ID: " + dto.consultationId()));

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());

        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de consulta não encontrado."));
        consultation.setConsultationType(consultationType);

        consultation = repository.save(consultation);

        updateConsultationMaterials(dto, consultation);

        return consultation;
    }

    private void updateConsultationMaterials(UpdateConsultationDTO dto, ConsultationEntity consultation) {
        for (MaterialConsultationDTO materialDTO : dto.materials()){
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + materialDTO.materialId()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialEntity consultationMaterialEntity = consultationMaterialRepository.findByConsultationId(consultation.getId(), id);
            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.materialId(), material);
        }
    }

    public void finalizarConsulta(UUID id) {

        ConsultationEntity consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com o ID: " + id));
        consultation.setConcluded(true);

        consultationRepository.save(consultation);

        List<ConsultationMaterialEntity> consultationMaterialEntity = consultationMaterialRepository.findByIdReturnId(id);

        for (ConsultationMaterialEntity entity : consultationMaterialEntity) {
            MaterialEntity material = materialRepository.findById(entity.getMaterial().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + entity.getMaterial().getId()));

            getTotalFutureMaterialQuantity(material.getId(), material);
        }
    }

    private void getTotalFutureMaterialQuantity(UUID materialId, MaterialEntity material) {
        Integer schedule_quantity = consultationMaterialRepository.sumMaterialScheduleQuantity(material);

        if (schedule_quantity == null) {
            schedule_quantity = 0;
        }

        materialCrudService.setScheduleQuantity( materialId, schedule_quantity);
    }

}
