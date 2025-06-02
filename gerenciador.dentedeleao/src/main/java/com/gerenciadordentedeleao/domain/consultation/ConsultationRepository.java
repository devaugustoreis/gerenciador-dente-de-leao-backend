package com.gerenciadordentedeleao.domain.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO dente_de_leao_manager.consultation_materials (consultation_id, material_id, quantity) VALUES (:consultation_id, :material_id, :quantity)",
            nativeQuery = true)
    void insertConsultationMaterial(@Param("consultation_id") UUID consultation_id,
                                    @Param("material_id") UUID material_id,
                                    @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query(value = "UPDATE dente_de_leao_manager.consultation_materials SET material_id = :material_id, quantity = :quantity WHERE consultation_id = :consultation_id",
            nativeQuery = true)
    void updateConsultationMaterial(@Param("consultation_id") UUID consultation_id,
                                    @Param("material_id") UUID material_id,
                                    @Param("quantity") Integer quantity);

    @Query("SELECT SUM(cm.quantity) FROM ConsultationMaterialEntity cm WHERE cm.material_id = :material_id AND cm.consultation_id IN (SELECT c.id FROM ConsultationEntity c WHERE c.concluded is false)")
    Integer getScheduleQuantity(@Param("material_id") UUID material_id);
}
