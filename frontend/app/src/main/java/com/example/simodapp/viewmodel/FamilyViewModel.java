package com.example.simodapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.dto.FamilyRequest;
import com.example.simodapp.domain.enums.Kinship;
import com.example.simodapp.domain.enums.StrokeTypes;

import java.util.ArrayList;
import java.util.List;

public class FamilyViewModel extends ViewModel {
    private final MutableLiveData<List<FamilyRequest>> contacts = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<FamilyRequest>> contactsFamily = contacts;

    public void addContacts(String name, String telephone, Kinship kinship){

        List<FamilyRequest> currentList = new ArrayList<>(contacts.getValue());
        currentList.add(new FamilyRequest(name,telephone,kinship));
        contacts.setValue(currentList);

    }

    public void removeContacts(int position){
        List<FamilyRequest> currentLista = new ArrayList<>(contacts.getValue());
        if(position>=0 && position<currentLista.size()){
            currentLista.remove(position);
            contacts.setValue(currentLista);

        }

    }

    public List<FamilyRequest> getShippingList(){
        return contacts.getValue();
    }

}
