package com.gerenciadordentedeleao.domain.consultation.material;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class ConsultationMaterialKey implements Serializable {

    @Column(name = "consultation_id")
    private UUID consultationId;

    @Column(name = "material_id")
    private UUID materialId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultationMaterialKey that = (ConsultationMaterialKey) o;
        return consultationId.equals(that.consultationId) && materialId.equals(that.materialId);
    }

    @Override
    public int hashCode() {
        return consultationId.hashCode() + materialId.hashCode();
    }
}
