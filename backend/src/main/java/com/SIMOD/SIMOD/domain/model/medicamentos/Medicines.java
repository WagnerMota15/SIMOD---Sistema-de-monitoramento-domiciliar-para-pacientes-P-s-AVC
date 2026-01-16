package com.SIMOD.SIMOD.domain.model.medicamentos;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.relatorio.Report;
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
    private UUID idMedicine;
    private String name;
    private BigDecimal dosage;
    private String unity;
    private int frequency;
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @ManyToMany
    @JoinTable(
            name = "medicines_has_report",
            joinColumns = @JoinColumn(name = "medicine_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}