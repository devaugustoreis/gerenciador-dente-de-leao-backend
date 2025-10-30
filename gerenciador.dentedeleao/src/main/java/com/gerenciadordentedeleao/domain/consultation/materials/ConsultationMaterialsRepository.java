package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ConsultationMaterialsRepository extends JpaRepository<ConsultationMaterialsEntity, UUID> {
    @Query("""
            SELECT COALESCE(SUM(consultationMaterial.quantity), 0)
            FROM ConsultationMaterialEntity consultationMaterial
            WHERE consultationMaterial.material = :material AND consultationMaterial.consultation.concluded = false
            """)
    Integer countByMaterialId(@Param("material") MaterialEntity material);

    @Query("""
            SELECT consultationMaterial
            FROM ConsultationMaterialEntity consultationMaterial
            WHERE consultationMaterial.consultation.id = :id
            """)
    List<ConsultationMaterialsEntity> findByIdReturnId(@Param("id") UUID id);

    @Query(
            """
            SELECT consultationMaterial.quantity, FUNCTION('DATE', consultationMaterial.consultation.endDate)
            FROM ConsultationMaterialEntity consultationMaterial
            WHERE consultationMaterial.material = :material
            AND consultationMaterial.consultation.concluded = false
            ORDER BY consultationMaterial.consultation.startDate
            """
    )
    List<Object[]> getMaterialsQuantities(@Param("material") MaterialEntity material);

}
