package com.gerenciadordentedeleao.domain.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "notifications")
@Table(name = "notifications")
@Setter
public class NotificationEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private UUID id;

    @Getter
    @Column(name = "message")
    private String message;
}
