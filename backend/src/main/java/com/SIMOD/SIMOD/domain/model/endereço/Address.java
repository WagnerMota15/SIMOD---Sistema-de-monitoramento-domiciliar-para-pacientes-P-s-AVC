package com.SIMOD.SIMOD.domain.model.endereço;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    private UUID id;

    private String cep;
    private String publicSpace;
    private String neighborhood;
    private String city;
    private String state;
    private String number;

    @OneToMany(mappedBy = "address")
    private Set<Patient> patients = new HashSet<>();

    public Address(String cep, String publicSpace, String neighborhood, String city, String state, String number) {
        this.cep = cep;
        this.publicSpace = publicSpace;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.number = number;
    }

}