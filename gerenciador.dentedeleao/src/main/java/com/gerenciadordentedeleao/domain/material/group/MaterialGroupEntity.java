package com.gerenciadordentedeleao.domain.material.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import java.util.UUID;

@Entity(name = "materials_groups")
@Table(name = "materials_groups")
@Setter
@Filter(name="SOFT_EXCLUSION")
@FilterDef(name = "SOFT_EXCLUSION", defaultCondition = "excluded = false")
public class MaterialGroupEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Getter
    @Column(name = "label")
    private String label;

    @Column(name = "excluded")
    private Boolean excluded = false;
}
