package com.gerenciadordentedeleao.domain.consultation.type;

import com.gerenciadordentedeleao.application.abstractions.PersistableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.UUID;

@Entity(name = "ConsultationTypeEntity")
@Table(name = "consultation_types")
@Setter
@Getter
@Filter(name = "SOFT_EXCLUSION", condition = "excluded = false")
public class ConsultationTypeEntity implements PersistableEntity {

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
    public boolean setAsDeleted() {
        this.excluded = true;
        return true;
    }
}
