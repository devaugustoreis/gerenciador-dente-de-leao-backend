package com.gerenciadordentedeleao.domain.material.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaterialGroupRepository extends JpaRepository<MaterialGroupEntity, UUID> {
}
