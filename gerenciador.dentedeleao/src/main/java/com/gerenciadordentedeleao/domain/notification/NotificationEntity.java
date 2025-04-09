package com.gerenciadordentedeleao.domain.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "notifications")
@Table(name = "notifications")
@Setter
@Getter
public class NotificationEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;


    @Column(name = "message")
    private String message;
}
