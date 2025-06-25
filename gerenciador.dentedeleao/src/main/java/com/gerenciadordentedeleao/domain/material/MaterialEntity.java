package com.gerenciadordentedeleao.domain.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "MaterialEntity")
@Table(name = "materials")
@Setter
@Getter
@FilterDef(name = "SOFT_EXCLUSION", defaultCondition = "excluded = false")
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class MaterialEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "excluded")
    @JsonIgnore
    private Boolean excluded = false;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "scheduled_quantity")
    private Integer scheduledQuantity;

    @Column(name = "expected_end_date")
    private Date expectedEndDate;

    @Column(name = "alert_quantity")
    private Integer alertQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "material",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MaterialHistoricEntity> materialHistoric = new ArrayList<>();

    @Lob
    @Column(name = "imagem")
    private byte[] image;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
