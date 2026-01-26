package com.SIMOD.SIMOD.domain.model.medicamentos;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.relatorio.Report;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicines {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_medicine")
    private UUID idMedicine;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal dosage;

    @Column(nullable = false, length = 10)
    private String unity;

    @Column(nullable = false)
    private int frequency;

    @Column(length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToMany
    @JoinTable(
            name = "medicines_has_report",
            joinColumns = @JoinColumn(name = "medicine_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}