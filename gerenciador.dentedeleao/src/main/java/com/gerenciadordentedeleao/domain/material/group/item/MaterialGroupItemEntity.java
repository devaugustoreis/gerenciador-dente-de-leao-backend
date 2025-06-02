package com.gerenciadordentedeleao.domain.material.group.item;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.group.MaterialGroupEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "MaterialGroupItemEntity")
@Table(name = "materials_groups_items")
public class MaterialGroupItemEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "material_group_id")
    private MaterialGroupEntity materialGroupEntity;

    @Id
    @ManyToOne
    @JoinColumn(name = "material_id")
    private MaterialEntity material;

    @Column(name = "quantity")
    private Integer quantity;

}

