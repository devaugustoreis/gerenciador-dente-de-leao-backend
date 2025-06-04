package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
            WHERE consultationMaterial.id = :id
            AND consultationMaterial.consultation.id = :consultationId
            """)
    ConsultationMaterialEntity findByConsultationId(@Param("consultationId") UUID consultationId, @Param("id") ConsultationMaterialsId id);

    @Query("""
            SELECT consultationMaterial
            FROM ConsultationMaterialEntity consultationMaterial
            WHERE consultationMaterial.consultation.id = :id
            """)
    List<ConsultationMaterialEntity> findByIdReturnId(@Param("id") UUID id);
}
