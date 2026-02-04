package com.SIMOD.SIMOD.domain.model.usuario;

import com.SIMOD.SIMOD.domain.enums.Platform;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;
}

