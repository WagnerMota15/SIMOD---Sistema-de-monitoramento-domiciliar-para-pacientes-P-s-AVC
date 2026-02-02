package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.repositories.AddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void create(AddressRequest request){
        Address address = new Address(request.cep(), request.publicSpace(), request.neighborhood(), request.city(), request.state(), request.number());
        addressRepository.save(address);

    }

}
