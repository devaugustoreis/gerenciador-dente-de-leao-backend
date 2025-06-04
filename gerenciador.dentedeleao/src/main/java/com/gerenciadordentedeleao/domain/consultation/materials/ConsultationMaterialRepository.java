package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ConsultationMaterialRepository extends JpaRepository<ConsultationMaterialEntity, UUID> {
    @Query("""
            SELECT SUM(consultationMaterial.quantity)
            FROM ConsultationMaterialEntity consultationMaterial
            WHERE consultationMaterial.material = :material AND consultationMaterial.consultation.concluded = false
            """)
    Integer sumMaterialScheduleQuantity(@Param("material") MaterialEntity material);

    @Query("""
            SELECT consultationMaterial
            FROM ConsultationMaterialEntity consultationMaterial
            WHERE consultationMaterial.consultation.id = :consultationId
            """)
    ConsultationMaterialEntity findByConsultationId(@Param("consultationId") UUID consultationId);
}
