package com.gerenciadordentedeleao.domain.material.historic;

import com.gerenciadordentedeleao.domain.material.stock.MaterialStockEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "materials_historic")
@Table(name = "materials_historic")
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
