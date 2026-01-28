package com.SIMOD.SIMOD.domain.model.mensagens;

import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notifications {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    private String title;

    private String message;

    private String type;

    private boolean read = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

