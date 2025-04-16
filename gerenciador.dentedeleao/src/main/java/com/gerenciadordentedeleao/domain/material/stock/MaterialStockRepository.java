package com.gerenciadordentedeleao.domain.material.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaterialStockRepository extends JpaRepository<MaterialStockEntity, UUID> {
}
