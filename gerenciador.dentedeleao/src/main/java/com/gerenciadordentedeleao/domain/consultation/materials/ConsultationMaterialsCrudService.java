package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import com.gerenciadordentedeleao.domain.consultation.dto.ConsultationDTO;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ConsultationMaterialsCrudService {

    private final MaterialCrudService materialCrudService;

    private final ConsultationMaterialsRepository consultationMaterialRepository;;

    private final MaterialRepository materialRepository;

    public ConsultationMaterialsCrudService(MaterialCrudService materialCrudService, ConsultationMaterialsRepository consultationMaterialRepository, MaterialRepository materialRepository) {
        this.materialCrudService = materialCrudService;
        this.consultationMaterialRepository = consultationMaterialRepository;
        this.materialRepository = materialRepository;
    }

    public void getTotalFutureMaterialQuantity(UUID materialId, MaterialEntity material) {
        Integer schedule_quantity = consultationMaterialRepository.countByMaterialId(material);

        if (schedule_quantity == null) {
            schedule_quantity = 0;
        }

        materialCrudService.setScheduleQuantity( materialId, schedule_quantity);
    }

    public void createConsultationMaterials(ConsultationDTO dto, ConsultationEntity consultation) {
        for (MaterialConsultationDTO materialDTO : dto.materials()) {
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", materialDTO.materialId()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialsEntity consultationMaterialEntity = new ConsultationMaterialsEntity();
            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setConsultation(consultation);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.materialId(), material);

            Date endDate = new Date(dto.endDate().getTime());
            materialCrudService.setExpectedEndDate(material, endDate);
        }
    }

    public void updateConsultationMaterials(ConsultationDTO dto, ConsultationEntity consultation) {
        for (MaterialConsultationDTO materialDTO : dto.materials()){
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", materialDTO.materialId()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialsEntity consultationMaterialEntity = consultationMaterialRepository.findByConsultationId(consultation.getId(), id);
            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.materialId(), material);

            Date endDate = new Date(dto.endDate().getTime());
            materialCrudService.setExpectedEndDate(material, endDate);
        }
    }

}
