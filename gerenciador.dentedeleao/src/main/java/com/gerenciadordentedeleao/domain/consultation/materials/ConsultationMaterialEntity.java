package com.gerenciadordentedeleao.domain.consultation.materials;

import com.gerenciadordentedeleao.domain.consultation.ConsultationEntity;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "ConsultationMaterialEntity")
@Table(name = "consultation_materials")
@Setter
@Getter
public class ConsultationMaterialEntity {

    @EmbeddedId
    private ConsultationMaterialsId id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @MapsId("consultationId")
    @JoinColumn(name = "consultation_id", referencedColumnName = "id")
    private ConsultationEntity consultation;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    private MaterialEntity material;

}
