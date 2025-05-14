package com.gerenciadordentedeleao.domain.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "materials")
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

    @Column(name = "alert_quantity")
    private Integer alertQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "material",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialHistoricEntity> materialHistoric = new ArrayList<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
