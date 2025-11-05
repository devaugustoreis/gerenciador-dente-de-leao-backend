package com.gerenciadordentedeleao.domain.consultation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {

    Page<ConsultationEntity> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<ConsultationEntity> findByEndDateBeforeAndStatus(LocalDateTime now, ConsultationStatus status, Pageable pageable);
}
