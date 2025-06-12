package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.PersistableEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;


@Entity(name = "ConsultationEntity")
@Table(name = "consultations")
@Setter
@Getter
public class ConsultationEntity implements PersistableEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    private UUID id;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "concluded")
    private Boolean concluded = false;

    @ManyToOne
    @JoinColumn(name = "consultation_type_id", referencedColumnName = "id")
    private ConsultationTypeEntity consultationType;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean setAsDeleted() {
        return false;
    }

}
