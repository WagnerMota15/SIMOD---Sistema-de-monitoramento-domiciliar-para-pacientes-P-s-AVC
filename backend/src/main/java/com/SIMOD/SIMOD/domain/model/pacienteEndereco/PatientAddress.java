package com.SIMOD.SIMOD.domain.model.pacienteEndereco;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_has_patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientAddress {

    @EmbeddedId
    private PatientAddressId id = new PatientAddressId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientId")
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("addressId")
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private boolean principal;

}
