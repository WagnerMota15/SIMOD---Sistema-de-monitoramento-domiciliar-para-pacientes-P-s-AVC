package com.SIMOD.SIMOD.domain.model.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

// Representa os dispositivos associados a um usuário, servirá para o push de notificações

@Entity
@Table(name = "user_devices")
@Getter
@Setter
public class UserDevices {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String fcmToken;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;
}

