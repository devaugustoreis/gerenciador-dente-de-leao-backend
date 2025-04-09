package com.gerenciadordentedeleao.domain.materialHistoric;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "materials_stock")
@Table(name = "materials_stock")
@Setter
public class MaterialHistoricEntity {

    // todo: adicionar foreign key material_stock_id e revisar datas

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Getter
    @Column(name = "movement_type")
    private String movementType;

    @Column(name = "quantity")
    @Getter
    private Integer quantity;

    @Getter
    @Column(name = "movement_date")
    private Timestamp movementDate;
}
