package com.SIMOD.SIMOD.domain.model.pacienteEndereco;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PatientAddressId implements Serializable {
    private UUID patientId;
    private UUID addressId;

    public PatientAddressId (){}

    public PatientAddressId(UUID patientId, UUID addressId) {
        this.patientId = patientId;
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientAddressId that)) return false;
        return Objects.equals(patientId, that.patientId)
                && Objects.equals(addressId, that.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, addressId);
    }
}
