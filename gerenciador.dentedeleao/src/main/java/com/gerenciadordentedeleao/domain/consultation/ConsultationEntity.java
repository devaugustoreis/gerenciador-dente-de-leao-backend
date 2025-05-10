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
public class ConsultationEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Getter
    @Column(name = "patient_name")
    private String patientName;

    @Getter
    @Column(name = "start_date")
    private Timestamp startDate;

    @Getter
    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "concluded")
    private Boolean concluded = false;

    @ManyToOne
    @JoinColumn(name = "consultation_type_id", referencedColumnName = "id")
    @Getter
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
