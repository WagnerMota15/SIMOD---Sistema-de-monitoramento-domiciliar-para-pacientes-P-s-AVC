package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.pacienteEndereco.PatientAddress;
import com.SIMOD.SIMOD.domain.model.pacienteEndereco.PatientAddressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientAddressRepository extends JpaRepository<PatientAddress, PatientAddressId> {
}
