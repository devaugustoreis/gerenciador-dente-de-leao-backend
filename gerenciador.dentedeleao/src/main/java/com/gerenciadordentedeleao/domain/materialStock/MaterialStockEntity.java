package com.gerenciadordentedeleao.domain.materialStock;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "materials_stock")
@Table(name = "materials_stock")
@Setter
public class MaterialStockEntity {

    // todo: adicionar foreign key material_id

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Column(name = "stock_quantity")
    @Getter
    private Integer stockQuantity;

    @Column(name = "scheduled_quantity")
    @Getter
    private Integer scheduledQuantity;

    @Column(name = "alert_quantity")
    @Getter
    private Integer alertQuantity;
}
