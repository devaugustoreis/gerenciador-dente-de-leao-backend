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
        return
                (consultationId != null ? consultationId.equals(that.consultationId) : that.consultationId == null) &&
                        (materialId != null ? materialId.equals(that.materialId) : that.materialId == null);
    }

    @Override
    public int hashCode() {
        int result = (consultationId != null ? consultationId.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        return result;
    }
}
