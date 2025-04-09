package com.gerenciadordentedeleao.domain.materialsGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "materials_groups")
@Table(name = "materials_groups")
@Setter
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
