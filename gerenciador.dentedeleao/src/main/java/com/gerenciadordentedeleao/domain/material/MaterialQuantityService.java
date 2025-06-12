package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialQuantityService {

    private final ConsultationMaterialsRepository consultationMaterialsRepository;

    @Autowired
    public MaterialQuantityService(ConsultationMaterialsRepository consultationMaterialsRepository) {
        this.consultationMaterialsRepository = consultationMaterialsRepository;
    }

    public int calculateScheduledQuantity(MaterialEntity material) {
        return consultationMaterialsRepository.countByMaterialId(material);
    }
}
