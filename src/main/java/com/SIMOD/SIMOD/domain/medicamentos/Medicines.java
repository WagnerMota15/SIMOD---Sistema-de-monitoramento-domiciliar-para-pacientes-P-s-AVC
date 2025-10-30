package com.SIMOD.SIMOD.domain.medicamentos;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.relatorio.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "medicines")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Medicines {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private BigDecimal dosagem;
    private String unidade;
    private int frequencia;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_numCouncil")
    private Professional professional;

    @ManyToMany
    @JoinTable(
            name = "medicine_report",
            joinColumns = @JoinColumn(name = "medicine_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}