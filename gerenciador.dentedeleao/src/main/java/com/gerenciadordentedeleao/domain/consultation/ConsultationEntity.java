package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.sql.Timestamp;
import java.util.UUID;


@Entity(name = "consultations")
@Table(name = "consultations")
@Setter
@Getter
public class ConsultationEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private ConsultationTypeEntity consultationTypeId;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
