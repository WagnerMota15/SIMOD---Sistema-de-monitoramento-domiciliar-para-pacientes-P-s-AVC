package com.example.simodapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.dto.AddressRequest;
import com.example.simodapp.data.repository.AddressRepository;

public class AddressViewModel extends ViewModel {
    private final AddressRepository addressRepository;

    public AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void sendAddress(AddressRequest request,Runnable onSucess,java.util.function.Consumer<String> onError){
        addressRepository.saveAddress(request,onSucess,onError);
    }
}
