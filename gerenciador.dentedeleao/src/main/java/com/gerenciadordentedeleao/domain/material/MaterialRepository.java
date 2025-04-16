package com.gerenciadordentedeleao.domain.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, UUID> {
}
