package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.pacienteEndereco.PatientAddress;
import com.SIMOD.SIMOD.domain.model.pacienteEndereco.PatientAddressId;
import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.dto.endereco.AddressResponse;
import com.SIMOD.SIMOD.repositories.AddressRepository;
import com.SIMOD.SIMOD.repositories.PatientAddressRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PatientAddressService {

    public PatientRepository patientRepository;
    public PatientAddressRepository patientAddressRepository;
    public AddressRepository addressRepository;


    @Transactional
    public AddressResponse addAddressToPatient(UUID patientId, AddressRequest request) {

        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Address address = new Address();
        address.setCep(request.cep());
        address.setNumber(request.number());
        address.setDescription(request.description());
        addressRepository.save(address);

        PatientAddress link = new PatientAddress();
        link.setId(new PatientAddressId(patient.getIdUser(), address.getId()));
        link.setPatient(patient);
        link.setAddress(address);
        link.setPrincipal(request.principal());

        patientAddressRepository.save(link);

        return new AddressResponse(
                address.getId(),
                address.getCep(),
                address.getNumber(),
                address.getDescription(),
                request.principal()
        );

    }
}
