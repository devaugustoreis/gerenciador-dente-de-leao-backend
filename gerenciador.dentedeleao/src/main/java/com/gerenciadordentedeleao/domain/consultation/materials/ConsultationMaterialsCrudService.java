package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
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

     public ConsultationMaterialsCrudService(@Lazy MaterialCrudService materialCrudService, ConsultationMaterialsRepository consultationMaterialRepository, MaterialRepository materialRepository) {
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

    public List<ConsultationMaterialsEntity> createConsultationMaterials(PayloadConsultationDTO dto, ConsultationEntity consultation) {
        List<ConsultationMaterialsEntity> materials = new ArrayList<>();

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
            materials.add(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.materialId(), material);

            Date endDate = new Date(dto.endDate().getTime());
            materialRepository.save(materialCrudService.setExpectedEndDate(material, endDate));
        }

         return materials;
    }

    public void updateConsultationMaterials(PayloadConsultationDTO dto, ConsultationEntity consultation) {
        for (MaterialConsultationDTO materialDTO : dto.materials()){
            MaterialEntity material = materialRepository.findById(materialDTO.materialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", materialDTO.materialId()));

            ConsultationMaterialsId id = new ConsultationMaterialsId();
            id.setConsultationId(consultation.getId());
            id.setMaterialId(material.getId());

            ConsultationMaterialsEntity consultationMaterialEntity = consultationMaterialRepository.findByConsultationId(consultation.getId(), id);

            if (materialDTO.quantity() == 0) {
                consultationMaterialRepository.deleteByConsultationAndMaterial(consultation.getId(), material.getId());
            }

            consultationMaterialEntity.setId(id);
            consultationMaterialEntity.setMaterial(material);
            consultationMaterialEntity.setQuantity(materialDTO.quantity());

            consultationMaterialRepository.save(consultationMaterialEntity);

            getTotalFutureMaterialQuantity(materialDTO.materialId(), material);

            Date endDate = new Date(dto.endDate().getTime());
            materialRepository.save(materialCrudService.setExpectedEndDate(material, endDate));
        }
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

//    private void delete(ConsultationMaterialsEntity consultationMaterialEntity) {
//        try {
//            consultationMaterialRepository.delete(consultationMaterialEntity);
//        } catch (DataIntegrityViolationException e) {
//            if (!(e.getCause() instanceof ConstraintViolationException cve && "23503".equals(cve.getSQLState()))) {
//                throw new BusinessException("Erro ao excluir o registro: " + e.getMessage(), e);
//            }
//            var entity = consultationMaterialRepository.findById(id).orElseThrow(() -> new BusinessException("Consulta não econtrado com o ID: %s para realizar a exclusão".formatted(id)));
//            var markedAsDeleted = entity.setAsDeleted();
//            if (markedAsDeleted) {
//                consultationMaterialRepository.save(entity);
//            }
//        } catch (Exception e) {
//            throw new BusinessException("Erro ao excluir o registro: " + e.getMessage(), e);
//        }
//    }

}
