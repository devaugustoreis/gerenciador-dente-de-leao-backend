package com.gerenciadordentedeleao.domain.consultation.materials;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
public class ConsultationMaterialsId implements Serializable {
    private UUID consultationId;
    private UUID materialId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultationMaterialsId that = (ConsultationMaterialsId) o;
        return consultationId.equals(that.consultationId) && materialId.equals(that.materialId);
    }

    @Override
    public int hashCode() {
        return consultationId.hashCode() + materialId.hashCode();
    }
}
