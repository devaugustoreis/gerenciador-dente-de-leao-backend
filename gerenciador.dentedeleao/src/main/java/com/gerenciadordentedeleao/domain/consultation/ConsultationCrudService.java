package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.errorhandler.BusinessException;
import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.consultation.dto.PayloadConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.ResponseConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsCrudService;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsEntity;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsRepository;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeRepository;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.historic.MovementType;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConsultationCrudService {

    private final ConsultationMaterialsCrudService consultationMaterialsCrudService;

    private final ConsultationMaterialsRepository consultationMaterialRepository;

    private final ConsultationTypeRepository consultationTypeRepository;

    private final MaterialRepository materialRepository;

    private final ConsultationRepository consultationRepository;

    private final MaterialCrudService materialCrudService;

    public ConsultationCrudService(ConsultationRepository consultationRepository, ConsultationMaterialsCrudService consultationMaterialsCrudService, ConsultationMaterialsRepository consultationMaterialRepository, ConsultationTypeRepository consultationTypeRepository, MaterialRepository materialRepository, MaterialCrudService materialCrudService) {
        this.consultationMaterialsCrudService = consultationMaterialsCrudService;
        this.consultationMaterialRepository = consultationMaterialRepository;
        this.consultationTypeRepository = consultationTypeRepository;
        this.materialRepository = materialRepository;
        this.consultationRepository = consultationRepository;
        this.materialCrudService = materialCrudService;
    }

    public ResponseConsultationDTO findById(UUID id) {
        var consultation = consultationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));
        return createResponseConsultationDTO(consultation);
    }

    public List<ResponseConsultationDTO> findAll(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        var consultations = consultationRepository.findByStartDateBetween(startOfDay, endOfDay, pageable);
        return consultations.stream().map(ConsultationCrudService::createResponseConsultationDTO).toList();
    }

    public Page<ConsultationEntity> findByStatusScheduled(Pageable pageable) {
        return consultationRepository.findByEndDateBeforeAndStatus(LocalDateTime.now(), ConsultationStatus.SCHEDULED, pageable);
    }

    public ResponseConsultationDTO create(PayloadConsultationDTO dto) {
        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta", "ID", dto.consultationTypeId()));

        ConsultationEntity consultation = new ConsultationEntity();

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());
        consultation.setConsultationType(consultationType);
        consultation.setStatus(dto.status());
        consultation = consultationRepository.save(consultation);

        List<ConsultationMaterialsEntity> materials = consultationMaterialsCrudService.createConsultationMaterials(dto, consultation);

        consultation.setMaterials(materials);
        consultation = consultationRepository.save(consultation);

        return createResponseConsultationDTO(consultation);
    }

    public ResponseConsultationDTO update(PayloadConsultationDTO dto, UUID id) {
        ConsultationEntity consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));

        ConsultationTypeEntity consultationType = consultationTypeRepository.findById(dto.consultationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta", "ID", dto.consultationTypeId()));

        consultation.setPatientName(dto.patientName());
        consultation.setStartDate(dto.startDate());
        consultation.setEndDate(dto.endDate());
        consultation.setConsultationType(consultationType);
        consultation = consultationRepository.save(consultation);
        consultation.setStatus(dto.status());

        List<ConsultationMaterialsEntity> materials = consultationMaterialsCrudService.updateConsultationMaterials(dto, consultation);

        consultation.getMaterials().clear();
        consultation.getMaterials().addAll(materials);
        consultation = consultationRepository.save(consultation);

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
                consultation.getStatus(),
                consultation.getStatus() == ConsultationStatus.CONCLUDED
        );
    }

    public void finalizarConsulta(UUID id) {
        ConsultationEntity consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "ID", id));
        consultation.setStatus(ConsultationStatus.CONCLUDED);

        consultationRepository.save(consultation);

        List<ConsultationMaterialsEntity> consultationMaterialEntity = consultationMaterialRepository.findByIdReturnId(id);

        for (ConsultationMaterialsEntity entity : consultationMaterialEntity) {
            MaterialEntity material = materialRepository.findById(entity.getMaterial().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", entity.getMaterial().getId()));

            consultationMaterialsCrudService.getTotalFutureMaterialQuantity(material.getId(), material);

            MovementStockDTO movementStockDTO = new MovementStockDTO(MovementType.REMOVAL, entity.getQuantity());
            materialCrudService.movementStock(material.getId(), movementStockDTO);
        }
    }

    public void delete(UUID id) {
        try {
            consultationRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            if (!(e.getCause() instanceof ConstraintViolationException cve && "23503".equals(cve.getSQLState()))) {
                throw new BusinessException("Erro ao excluir o registro: " + e.getMessage(), e);
            }
            var entity = consultationRepository.findById(id).orElseThrow(() -> new BusinessException("Consulta não econtrado com o ID: %s para realizar a exclusão".formatted(id)));
            var markedAsDeleted = entity.setAsDeleted();
            if (markedAsDeleted) {
                consultationRepository.save(entity);
            }
        } catch (Exception e) {
            throw new BusinessException("Erro ao excluir o registro: " + e.getMessage(), e);
        }
    }

}
