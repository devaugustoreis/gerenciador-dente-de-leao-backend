package com.gerenciadordentedeleao.domain.material.group.item;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialGroupItemKey implements Serializable {

    @Column(name = "material_group_id")
    private UUID materialGroupId;

    @Column(name = "material_id")
    private UUID materialId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialGroupItemKey that = (MaterialGroupItemKey) o;
        return materialGroupId.equals(that.materialGroupId) && materialId.equals(that.materialId);
    }

    @Override
    public int hashCode() {
        return materialGroupId.hashCode() + materialId.hashCode();
    }
}
