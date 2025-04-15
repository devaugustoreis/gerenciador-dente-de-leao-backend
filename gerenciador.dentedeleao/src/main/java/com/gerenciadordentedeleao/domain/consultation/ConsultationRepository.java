package com.gerenciadordentedeleao.domain.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {
}
