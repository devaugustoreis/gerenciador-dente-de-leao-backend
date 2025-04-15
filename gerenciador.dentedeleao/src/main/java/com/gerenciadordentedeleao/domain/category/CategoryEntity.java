package com.gerenciadordentedeleao.domain.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

@Entity(name = "categories")
@Table(name = "categories")
@Setter
@FilterDef(name = "SOFT_EXCLUSION", defaultCondition = "excluded = false", parameters = @ParamDef(name = "excluded", type = Boolean.class))
public class CategoryEntity {

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
