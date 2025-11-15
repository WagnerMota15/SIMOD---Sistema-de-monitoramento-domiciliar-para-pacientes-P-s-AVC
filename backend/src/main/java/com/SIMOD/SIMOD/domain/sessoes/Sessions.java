package com.SIMOD.SIMOD.domain.sessoes;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.relatorio.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sessions")  // OK, mas em alguns bancos "sessions" é reservado → use "session_records" se der problema
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sessions {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private LocalDateTime data;
    private Boolean remoto;
    private String local;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_num_council", nullable = false)
    private Professional professional;

    @ManyToMany
    @JoinTable(
            name = "session_report",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}