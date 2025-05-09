package com.gerenciadordentedeleao.domain.material.stock;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "materials_stock")
@Table(name = "materials_stock")
@Setter
@Getter
public class MaterialStockEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "scheduled_quantity")
    private Integer scheduledQuantity;

    @Column(name = "alert_quantity")
    private Integer alertQuantity;

    @OneToOne
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    private MaterialEntity materialId;
}
