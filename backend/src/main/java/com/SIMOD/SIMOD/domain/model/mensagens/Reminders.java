package com.SIMOD.SIMOD.domain.model.mensagens;

import com.SIMOD.SIMOD.domain.enums.IntervaloRecorrencia;
import com.SIMOD.SIMOD.domain.enums.TipoLembrete;
import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminders {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoLembrete type;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    private boolean recurring;

    @Enumerated(EnumType.STRING)
    @Column(name = "interval_type")
    private IntervaloRecorrencia intervalType;

    private boolean confirmed;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by", nullable = false)
    private RemetenteVinculo createdBy; // Reaproveitando

    private boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void confirm() {
        this.confirmed = true;
        this.confirmedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
    }
}
