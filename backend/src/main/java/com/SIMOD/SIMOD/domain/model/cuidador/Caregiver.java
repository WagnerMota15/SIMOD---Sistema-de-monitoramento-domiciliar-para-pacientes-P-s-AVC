package com.SIMOD.SIMOD.domain.model.cuidador;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "caregiver")
@DiscriminatorValue("CAREGIVER")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caregiver extends User {

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaregiverPatient> patientVinculos;

    public void adicionarVinculoPaciente(CaregiverPatient vinculo) {
        this.patientVinculos.add(vinculo);
        vinculo.setCaregiver(this);
    }
}