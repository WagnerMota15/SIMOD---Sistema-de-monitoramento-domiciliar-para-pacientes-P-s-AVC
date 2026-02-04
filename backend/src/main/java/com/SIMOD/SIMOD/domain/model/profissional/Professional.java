package com.SIMOD.SIMOD.domain.model.profissional;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professional")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Professional extends User {
    @Column(name = "num_council")
    private String numCouncil;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PatientProfessional> patientVinculos = new HashSet<>();

    public void adicionarVinculoPaciente(PatientProfessional vinculo) {
        this.patientVinculos.add(vinculo);
        vinculo.setProfessional(this);
    }
}