package com.gerenciadordentedeleao.domain.consultation.material;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsultationMaterialRepository extends JpaRepository<ConsultationMaterialEntity, UUID> {

    @Query("""
            select coalesce(sum(consultationMaterial.quantity), 0)
            from ConsultationMaterialEntity consultationMaterial
            where consultationMaterial.material = :material and consultationMaterial.consultation.concluded = false
            """)
    int countByMaterialId(@Param("material") MaterialEntity material);
}
