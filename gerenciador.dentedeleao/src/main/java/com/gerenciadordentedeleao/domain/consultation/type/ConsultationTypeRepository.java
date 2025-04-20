package com.gerenciadordentedeleao.domain.consultation.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsultationTypeRepository extends JpaRepository<ConsultationTypeEntity, UUID> {
}
