package com.gerenciadordentedeleao.domain.materialHistoric;

import com.gerenciadordentedeleao.domain.materialStock.MaterialStockEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "materials_stock")
@Table(name = "materials_stock")
@Setter
@Getter
public class MaterialHistoricEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "movement_type")
    private String movementType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "movement_date")
    private Timestamp movementDate;

    @ManyToOne
    @JoinColumn(name = "material_stock_id", referencedColumnName = "id")
    private MaterialStockEntity materialStockId;
}
