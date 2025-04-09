package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "materials")
@Table(name = "materials")
@Setter
public class MaterialEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Getter
    @Column(name = "name")
    private String name;

    @Column(name = "excluded")
    private Boolean excluded = false;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @Getter
    private CategoryEntity categoryId;
}
