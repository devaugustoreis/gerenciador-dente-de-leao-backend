package com.gerenciadordentedeleao.domain.consultation;

import com.gerenciadordentedeleao.application.abstractions.PersistableEntity;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsEntity;
import com.gerenciadordentedeleao.domain.consultation.type.ConsultationTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
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
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "concluded")
    private Boolean concluded = false;

    @ManyToOne
    @JoinColumn(name = "consultation_type_id", referencedColumnName = "id")
    private ConsultationTypeEntity consultationType;

    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationMaterialsEntity> materials;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean setAsDeleted() {
        return false;
    }

}
