package com.SIMOD.SIMOD.domain.model.associacoes;

import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "sender",nullable = false)
    private RemetenteVinculo remetente;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "response_date")
    private LocalDateTime dataResposta;

    @Column(name = "notes", length = 500)
    private String observacao;

    public void aceitar() {
        this.status = VinculoStatus.ACEITO;
        this.dataResposta = LocalDateTime.now();
    }

    public void rejeitar(String motivo) {
        this.status = VinculoStatus.REJEITADO;
        this.dataResposta = LocalDateTime.now();
        this.observacao = motivo;
    }

    public void cancelar() {
        this.status = VinculoStatus.CANCELADO;
    }
}