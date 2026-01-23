package com.SIMOD.SIMOD.domain.model.associacoes;

import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "caregiver_has_patient")
@IdClass(CaregiverPatientId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaregiverPatient {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VinculoStatus status = VinculoStatus.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RemetenteVinculo remetente;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(length = 500)
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
