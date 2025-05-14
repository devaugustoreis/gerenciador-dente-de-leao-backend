package com.gerenciadordentedeleao.domain.material.stock;

import com.gerenciadordentedeleao.domain.material.MaterialEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity(name = "materials_stock")
@Table(name = "materials_stock")
@Setter
@Getter
public class MaterialStockEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "uuid2")
    private UUID id;


}
