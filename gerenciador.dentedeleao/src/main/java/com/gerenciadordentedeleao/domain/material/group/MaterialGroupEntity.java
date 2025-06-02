package com.gerenciadordentedeleao.domain.material.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity(name = "MaterialGroupEntity")
@Table(name = "materials_groups")
@Setter
@Getter
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class MaterialGroupEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "label")
    private String label;

    @Column(name = "excluded")
    private Boolean excluded = false;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
