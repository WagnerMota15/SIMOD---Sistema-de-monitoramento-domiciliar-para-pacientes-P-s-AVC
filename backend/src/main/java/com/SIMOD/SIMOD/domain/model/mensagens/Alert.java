package com.SIMOD.SIMOD.domain.model.mensagens;

import com.SIMOD.SIMOD.domain.enums.NivelAlerta;
import com.SIMOD.SIMOD.domain.enums.TipoLembrete;
import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Enumerated(EnumType.STRING)
    private TipoNotificacao type;

    @Enumerated(EnumType.STRING)
    private NivelAlerta severity;

    private String title;
    private String description;

    private boolean resolved = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
