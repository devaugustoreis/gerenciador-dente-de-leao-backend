package com.gerenciadordentedeleao.domain.consultation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "consultations")
@Table(name = "consultations")
@Setter
public class ConsultationsEntity {

    // todo: revisar as datas e adicionar a foreign key consultation_type_id

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
}
