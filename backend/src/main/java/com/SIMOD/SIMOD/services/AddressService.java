package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.repositories.AddressRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final PatientRepository patientRepository;


    public AddressService(AddressRepository addressRepository, PatientRepository patientRepository) {
        this.addressRepository = addressRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public void create(UUID patientId,AddressRequest request){

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("PACIENTE NÃO ENCONTRADO"));

        Address address = addressRepository
                .findByCepAndPublicSpaceAndNumberAndNeighborhoodAndCityAndState(
                        request.cep(),
                        request.publicSpace(),
                        request.number(),
                        request.neighborhood(),
                        request.city(),
                        request.state()
                )
                .orElseGet(() -> {
                    Address newAddress = new Address(
                            request.cep(),
                            request.publicSpace(),
                            request.neighborhood(),
                            request.city(),
                            request.state(),
                            request.number()
                    );
                    return addressRepository.save(newAddress);
                });

        patient.setAddress(address);
        patientRepository.save(patient);

    }

}
