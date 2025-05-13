package com.gerenciadordentedeleao.domain.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity(name = "categories")
@Table(name = "categories")
@Setter
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class CategoryEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "uuid2")
    @Getter
    private UUID id;

    @Getter
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

    public void setExcluded(Boolean excluded) {
        this.excluded = excluded;
    }
}
