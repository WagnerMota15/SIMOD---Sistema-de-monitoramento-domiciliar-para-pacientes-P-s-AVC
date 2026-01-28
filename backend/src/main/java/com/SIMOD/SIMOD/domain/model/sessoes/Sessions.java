package com.SIMOD.SIMOD.domain.model.sessoes;

import com.SIMOD.SIMOD.domain.enums.Role;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.relatorio.Report;
import jakarta.persistence.*;
import lombok.*;
import com.SIMOD.SIMOD.domain.enums.SessionsStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Boolean remote;

    @Column(length = 80)
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionsStatus status ;

    @Column(name = "reason_change", length = 500)
    private String reasonChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by", length = 20)
    private Role criadoPorTipo;

    @ManyToMany
    @JoinTable(
            name = "sessions_has_report",
            joinColumns = @JoinColumn(name = "sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}