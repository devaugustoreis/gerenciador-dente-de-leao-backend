package com.gerenciadordentedeleao.domain.material.group;

import com.gerenciadordentedeleao.application.abstractions.PersistableEntity;
import com.gerenciadordentedeleao.domain.material.group.item.MaterialGroupItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "MaterialGroupEntity")
@Table(name = "materials_groups")
@Setter
@Getter
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class MaterialGroupEntity implements PersistableEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "label")
    private String label;

    @Column(name = "excluded")
    private Boolean excluded = false;

    @OneToMany(mappedBy = "materialGroup",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialGroupItemEntity> items = new ArrayList<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean setAsDeleted() {
        this.excluded = true;
        return true;
    }
}
