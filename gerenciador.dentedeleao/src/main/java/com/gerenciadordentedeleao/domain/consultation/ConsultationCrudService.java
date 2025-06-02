package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.consultation.dto.CreateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.dto.UpdateConsultationDTO;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeCrudService;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.dto.MaterialItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.reflect.Field;
import java.util.UUID;

@Service
public class ConsultationCrudService extends AbstractCrudService<ConsultationEntity> {

    private ConsultationTypeCrudService consultationTypeCrudService;

    private MaterialCrudService materialCrudService;

    private ConsultationRepository consultationRepository;

    public ConsultationCrudService(ConsultationRepository consultationRepository) {
        super(consultationRepository);
    }

    public ConsultationEntity create(CreateConsultationDTO dto) {
        ConsultationEntity consultation = new ConsultationEntity();

        consultation.setPatientName(dto.patient_name());
        consultation.setStartDate(dto.start_date());
        consultation.setEndDate(dto.end_date());

        ConsultationTypeEntity consultationType = consultationTypeCrudService.findById(dto.consultation_type_id())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de consulta não encontrado."));
        consultation.setConsultationTypeId(consultationType);

        consultation = repository.save(consultation);

        for (MaterialItemDTO materialDTO : dto.materials()){
            consultationRepository.insertConsultationMaterial(consultation.getId(), materialDTO.materialId(), materialDTO.quantity());

            Integer schedule_quantity = consultationRepository.getScheduleQuantity(materialDTO.materialId());
            materialCrudService.setScheduleQuantity( materialDTO.materialId(), schedule_quantity);
        }

        return consultation;
    }

    public ConsultationEntity update(UpdateConsultationDTO dto) {
        ConsultationEntity consultation = consultationRepository.findById(dto.consultation_id())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com o ID: " + dto.consultation_id()));

        consultation.setPatientName(dto.patient_name());
        consultation.setStartDate(dto.start_date());
        consultation.setEndDate(dto.end_date());

        ConsultationTypeEntity consultationType = consultationTypeCrudService.findById(dto.consultation_type_id())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de consulta não encontrado."));
        consultation.setConsultationTypeId(consultationType);

        consultation = repository.save(consultation);

        for (MaterialItemDTO materialDTO : dto.materials()){
            consultationRepository.updateConsultationMaterial(consultation.getId(), materialDTO.materialId(), materialDTO.quantity());

            Integer schedule_quantity = consultationRepository.getScheduleQuantity(materialDTO.materialId());
            materialCrudService.setScheduleQuantity( materialDTO.materialId(), schedule_quantity);
        }

        return consultation;
    }

    public void finalizarConsulta(UpdateConsultationDTO dto) {
        ConsultationEntity consultation = consultationRepository.findById(dto.consultation_id())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com o ID: " + dto.consultation_id()));
        consultation.setConcluded(true);

        for (MaterialItemDTO materialDTO : dto.materials()){
            Integer schedule_quantity = consultationRepository.getScheduleQuantity(materialDTO.materialId());
            materialCrudService.setScheduleQuantity( materialDTO.materialId(), schedule_quantity);
        }
    }

}
