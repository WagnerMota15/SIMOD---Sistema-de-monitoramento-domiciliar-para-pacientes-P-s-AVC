package com.SIMOD.SIMOD.domain.model.atividades;

import com.SIMOD.SIMOD.domain.enums.ActivitiesTypes;
import com.SIMOD.SIMOD.domain.enums.Status;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.relatorio.Report;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activities {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_exercise", nullable = false)
    private ActivitiesTypes type;

    @Column(name = "freq_recommended", nullable = false)
    private int freqRecommended;

    @Column(name = "video_url", length = 200)
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToMany
    @JoinTable(
            name = "activities_has_report",
            joinColumns = @JoinColumn(name = "activities_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}