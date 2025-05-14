package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.springframework.data.domain.Persistable;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "excluded")
    private Boolean excluded = false;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity categoryId;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
