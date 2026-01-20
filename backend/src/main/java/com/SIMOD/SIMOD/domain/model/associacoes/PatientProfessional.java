package com.SIMOD.SIMOD.domain.model.associacoes;

import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "professional_has_patient")
@IdClass(PatientProfessionalId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfessional {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VinculoStatus status = VinculoStatus.PENDENTE;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(length = 500)
    private String observacao;

    public void aceitar() { /* igual acima */ }
    public void rejeitar(String motivo) { /* igual acima */ }
}