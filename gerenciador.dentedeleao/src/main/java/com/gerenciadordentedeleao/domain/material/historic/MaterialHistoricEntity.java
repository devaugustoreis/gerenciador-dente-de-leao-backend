package com.gerenciadordentedeleao.domain.material.historic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "MaterialHistoricEntity")
@Table(name = "materials_historic")
@Setter
@Getter
public class MaterialHistoricEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type")
    private MovementType movementType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "movement_date")
    private LocalDateTime movementDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @JsonIgnore
    private MaterialEntity material;
}

