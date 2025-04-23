package com.gerenciadordentedeleao.domain.consultation.type;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import java.util.UUID;

@Entity(name = "consultation_types")
@Table(name = "consultation_types")
@Setter
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class ConsultationTypeEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Getter
    @Column(name = "label")
    private String label;

    @Column(name = "excluded")
    private Boolean excluded = false;
}
