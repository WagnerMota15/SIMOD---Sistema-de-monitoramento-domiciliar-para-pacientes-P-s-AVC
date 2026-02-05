package com.SIMOD.SIMOD.domain.model.familiares;

import com.SIMOD.SIMOD.domain.enums.Kinship;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "family")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family {

    public Family(String name, String telephone, Kinship kinship) {
        this.name = name;
        this.telephone = telephone;
        this.kinship = kinship;
    }

    public UUID getIdFamily() {
        return idFamily;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }

    public Kinship getKinship() {
        return kinship;
    }

    public Patient getPatient() {
        return patient;
    }

    @Id
    @GeneratedValue
    private UUID idFamily;
    private String name;
    private String telephone;

    @Enumerated(EnumType.STRING)
    private Kinship kinship;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


}