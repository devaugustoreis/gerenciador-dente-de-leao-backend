package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import com.gerenciadordentedeleao.domain.consultation.ConsultationRepository;
import com.gerenciadordentedeleao.domain.consultation.dto.PayloadConsultationDTO;
import com.gerenciadordentedeleao.domain.material.MaterialCrudService;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.MaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.MaterialConsultationDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ConsultationMaterialsCrudService {

    private final MaterialCrudService materialCrudService;

    private final ConsultationMaterialsRepository consultationMaterialRepository;;

    private final MaterialRepository materialRepository;

    private final ConsultationRepository consultationRepository;

     public ConsultationMaterialsCrudService(@Lazy MaterialCrudService materialCrudService, ConsultationMaterialsRepository consultationMaterialRepository, MaterialRepository materialRepository, ConsultationRepository consultationRepository) {
        this.materialCrudService = materialCrudService;
        this.consultationMaterialRepository = consultationMaterialRepository;
        this.materialRepository = materialRepository;
        this.consultationRepository = consultationRepository;
    }

    public void getTotalFutureMaterialQuantity(UUID materialId, MaterialEntity material) {
        Integer schedule_quantity = consultationMaterialRepository.countByMaterialId(material);

        if (schedule_quantity == null) {
            schedule_quantity = 0;
        }

        materialCrudService.setScheduleQuantity( materialId, schedule_quantity);
    }

    public List<ConsultationMaterialsEntity> createConsultationMaterials(PayloadConsultationDTO dto, ConsultationEntity consultation) {
        List<ConsultationMaterialsEntity> materials = new ArrayList<>();

         for (MaterialConsultationDTO materialDTO : dto.materials()) {
            MaterialEntity material = materialRepository.findById(materialDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", materialDTO.id()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialsEntity consultationMaterialEntity = new ConsultationMaterialsEntity();
            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setConsultation(consultation);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);
            materials.add(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.id(), material);

            materialRepository.save(materialCrudService.setExpectedEndDateAndHighlight(material));
        }

         return materials;
    }

    public List<ConsultationMaterialsEntity> updateConsultationMaterials(PayloadConsultationDTO dto, ConsultationEntity consultation) {
        List<ConsultationMaterialsEntity> materials = new ArrayList<>();

         for (MaterialConsultationDTO materialDTO : dto.materials()){

             if (materialDTO.quantity().equals(0)) {
                 continue;
             }

            MaterialEntity material = materialRepository.findById(materialDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", materialDTO.id()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialsEntity consultationMaterialEntity = new ConsultationMaterialsEntity();
            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setConsultation(consultation);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);
            materials.add(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.id(), material);

            materialRepository.save(materialCrudService.setExpectedEndDateAndHighlight(material));
        }

        return materials;
    }

    public Date findExpectedEndDate(MaterialEntity material){
        List<Object[]> quantityMaterialsList = consultationMaterialRepository.getMaterialsQuantities(material);
        int totalQuantity = 0;

        for (Object[] row : quantityMaterialsList){
            totalQuantity += (Integer) row[0];

            if (totalQuantity >= material.getStockQuantity()){
                return (Date) row[1];
            }
        }

        return null;
    }
}
