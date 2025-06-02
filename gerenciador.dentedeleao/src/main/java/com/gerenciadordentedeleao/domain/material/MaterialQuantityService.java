package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.consultation.material.ConsultationMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialQuantityService {

    private final ConsultationMaterialRepository consultationMaterialRepository;

    @Autowired
    public MaterialQuantityService(ConsultationMaterialRepository consultationMaterialRepository) {
        this.consultationMaterialRepository = consultationMaterialRepository;
    }

    public int calculateScheduledQuantity(MaterialEntity material) {
        return consultationMaterialRepository.countByMaterialId(material);
    }
}
