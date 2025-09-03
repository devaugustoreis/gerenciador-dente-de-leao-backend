package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.errorhandler.BusinessException;
import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.consultation.dto.PlayloadConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.ResponseConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsCrudService;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsEntity;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsRepository;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeRepository;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ConsultationCrudService {

    private final ConsultationMaterialsCrudService consultationMaterialsCrudService;

    private final ConsultationMaterialsRepository consultationMaterialRepository;

    private final ConsultationTypeRepository consultationTypeRepository;

    private final MaterialRepository materialRepository;

    private final ConsultationRepository consultationRepository;

    public ConsultationCrudService(ConsultationRepository consultationRepository, ConsultationMaterialsCrudService consultationMaterialsCrudService, ConsultationMaterialsRepository consultationMaterialRepository, ConsultationTypeRepository consultationTypeRepository, MaterialRepository materialRepository) {
        this.consultationMaterialsCrudService = consultationMaterialsCrudService;
        this.consultationMaterialRepository = consultationMaterialRepository;
        this.consultationTypeRepository = consultationTypeRepository;
        this.materialRepository = materialRepository;
        this.consultationRepository = consultationRepository;
    }

    public ResponseConsultationDTO findById(UUID id) {
        var consultation = consultationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));
        return createResponseConsultationDTO(consultation);
    }

    public List<ResponseConsultationDTO> findAll() {
        var consultations = consultationRepository.findAll();
        return consultations.stream().map(ConsultationCrudService::createResponseConsultationDTO).toList();
    }

    public ResponseConsultationDTO create(PlayloadConsultationDTO dto) {
        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta", "ID", dto.consultationTypeId()));

        ConsultationEntity consultation = new ConsultationEntity();

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());
        consultation.setConsultationType(consultationType);

        consultation = consultationRepository.save(consultation);

        consultationMaterialsCrudService.createConsultationMaterials(dto, consultation);

        return createResponseConsultationDTO(consultation);
    }


    public ResponseConsultationDTO update(PlayloadConsultationDTO dto, UUID id) {
        ConsultationEntity consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());

        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta", "ID", dto.consultationTypeId()));
        consultation.setConsultationType(consultationType);

        consultation = consultationRepository.save(consultation);

        consultationMaterialsCrudService.updateConsultationMaterials(dto, consultation);

        return createResponseConsultationDTO(consultation);
    }

    private static ResponseConsultationDTO createResponseConsultationDTO(ConsultationEntity consultation) {
        List<MaterialConsultationDTO> materials = consultation.getMaterials().stream()
                .map(m -> new MaterialConsultationDTO(m.getMaterial().getId(), m.getQuantity()))
                .toList();
        return new ResponseConsultationDTO(
                consultation.getPatientName(),
                consultation.getStartDate(),
                consultation.getEndDate(),
                materials,
                consultation.getConsultationType().getId(),
                consultation.getId(),
                consultation.getConcluded()
        );
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

}
