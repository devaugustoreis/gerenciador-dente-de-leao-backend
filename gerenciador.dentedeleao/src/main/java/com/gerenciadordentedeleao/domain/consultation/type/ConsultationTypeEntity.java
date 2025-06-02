package com.gerenciadordentedeleao.domain.consultation.type;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity(name = "ConsultationTypeEntity")
@Table(name = "consultation_types")
@Setter
@Getter
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class ConsultationTypeEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "label")
    private String label;

    @Column(name = "excluded")
    private Boolean excluded = false;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
