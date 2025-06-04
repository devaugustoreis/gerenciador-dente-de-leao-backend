package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.consultation.dto.CreateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.UpdateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialEntity;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialRepository;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeCrudService;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeRepository;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        consultation.setStartDate(dto.starDate());
        consultation.setEndDate(dto.endDate());
        consultation.setConsultationType(consultationType);

        consultation = repository.save(consultation);

        createConsultationMaterials(dto, consultation);

        return consultation;
    }

    private void createConsultationMaterials(CreateConsultationDTO dto, ConsultationEntity consultation) {
        for (MaterialConsultationDTO materialDTO : dto.materials()){
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + materialDTO.materialId()));

            ConsultationMaterialEntity consultationMaterialEntity = new ConsultationMaterialEntity();

            consultationMaterialEntity.setConsultation(consultation);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO, material);
        }
    }

    public ConsultationEntity update(UpdateConsultationDTO dto) {
        ConsultationEntity consultation = consultationRepository.findById(dto.consultation_id())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com o ID: " + dto.consultation_id()));

        consultation.setPatientName(dto.patient_name());
        consultation.setStartDate(dto.start_date());
        consultation.setEndDate(dto.end_date());

        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultation_type_id())
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

            ConsultationMaterialEntity consultationMaterialEntity = consultationMaterialRepository.findByConsultationId(consultation.getId());
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO, material);
        }
    }

    public void finalizarConsulta(UpdateConsultationDTO dto) {
        ConsultationEntity consultation = consultationRepository.findById(dto.consultation_id())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com o ID: " + dto.consultation_id()));
        consultation.setConcluded(true);

        for (MaterialConsultationDTO materialDTO : dto.materials()){
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + materialDTO.materialId()));

            getTotalFutureMaterialQuantity(materialDTO, material);
        }
    }

    private void getTotalFutureMaterialQuantity(MaterialConsultationDTO materialDTO, MaterialEntity material) {
        Integer schedule_quantity = consultationMaterialRepository.sumMaterialScheduleQuantity(material);
        materialCrudService.setScheduleQuantity( materialDTO.materialId(), schedule_quantity);
    }

}
