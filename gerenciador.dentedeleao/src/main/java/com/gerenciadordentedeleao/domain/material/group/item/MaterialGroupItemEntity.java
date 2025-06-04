package com.gerenciadordentedeleao.domain.material.group.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.group.MaterialGroupEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "MaterialGroupItemEntity")
@Table(name = "materials_groups_items")
@Getter
@Setter
public class MaterialGroupItemEntity {

    @EmbeddedId
    private MaterialGroupItemKey materialGroupItemId;

    @ManyToOne
    @MapsId("materialGroupId")
    @JoinColumn(name = "material_group_id")
    @JsonBackReference
    private MaterialGroupEntity materialGroup;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id")
    private MaterialEntity material;

    @Column(name = "quantity")
    private Integer quantity;

}
