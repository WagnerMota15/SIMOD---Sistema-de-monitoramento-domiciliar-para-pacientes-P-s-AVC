package com.example.simodapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.dto.AddressRequest;
import com.example.simodapp.data.dto.FamilyRequest;
import com.example.simodapp.data.repository.AddressFamilyRepository;
import com.example.simodapp.domain.enums.Kinship;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressFamilyViewModel extends ViewModel {

    private static final String TAG = "AddressFamilyVM";

    // LiveData para lista de familiares
    private final MutableLiveData<List<FamilyRequest>> familyList = new MutableLiveData<>();

    // LiveData para sucesso e erro (usados diretamente no repository)
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    // Repository
    private final AddressFamilyRepository repository;

    // Construtor (usado pela Factory)
    public AddressFamilyViewModel(AddressFamilyRepository repository) {
        this.repository = repository;
        Log.d(TAG, "AddressFamilyViewModel instanciado com sucesso");
    }

    // Getters
    public LiveData<List<FamilyRequest>> getFamilyList() {
        return familyList;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }

    public LiveData<String> getError() {
        return error;
    }

    /**
     * Método principal chamado ao clicar em "Finalizar"
     */
    public void finalizarCadastro(UUID patientId, AddressRequest address) {
        Log.d(TAG, "finalizarCadastro() iniciado");

        // Validações iniciais
        if (patientId == null) {
            Log.e(TAG, "patientId é NULL - abortando salvamento");
            error.postValue("ID do paciente não encontrado");
            return;
        }

        Log.d(TAG, "Patient ID recebido: " + patientId.toString());

        if (address == null) {
            Log.e(TAG, "AddressRequest é NULL - abortando");
            error.postValue("Dados do endereço inválidos");
            return;
        }

        // Log dos dados do endereço
        Log.d(TAG, "Dados do endereço a enviar:");
        Log.d(TAG, "CEP: " + address.getCep());
        Log.d(TAG, "Logradouro: " + address.getStreet());
        Log.d(TAG, "Bairro: " + address.getNeighborhood());
        Log.d(TAG, "Cidade: " + address.getCity());
        Log.d(TAG, "Estado: " + address.getState());
        Log.d(TAG, "Número: " + address.getNumber());

        // Lista de familiares
        List<FamilyRequest> families = familyList.getValue();
        int familyCount = families != null ? families.size() : 0;
        Log.d(TAG, "Quantidade de contatos familiares: " + familyCount);
        if (familyCount > 0) {
            Log.d(TAG, "Primeiro contato: " + families.get(0)); // ou personalize com toString()
        }

        try {
            Log.d(TAG, "Chamando repository.sendAddressAndFamily...");

            repository.sendAddressAndFamily(
                    patientId,
                    address,
                    families,
                    success,   // ← passa o LiveData diretamente
                    error      // ← passa o LiveData diretamente
            );

            Log.d(TAG, "Chamada ao repository enviada com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Exceção ao chamar repository.sendAddressAndFamily", e);
            error.postValue("Erro interno ao tentar salvar: " + e.getMessage());
        }
    }

    // Método para adicionar contato familiar
    public void addFamily(String nome, String telefone, Kinship kinship) {
        Log.d(TAG, "addFamily chamado: nome=" + nome + ", telefone=" + telefone + ", parentesco=" + kinship);

        List<FamilyRequest> current = familyList.getValue();
        if (current == null) {
            current = new ArrayList<>();
        }

        current.add(new FamilyRequest(nome, telefone, kinship)); // ajuste conforme sua classe
        familyList.postValue(current);

        Log.d(TAG, "Contato adicionado - total agora: " + current.size());
    }

    // Método para remover contato
    public void removeFamily(int position) {
        Log.d(TAG, "removeFamily chamado na posição: " + position);

        List<FamilyRequest> current = familyList.getValue();
        if (current != null && position >= 0 && position < current.size()) {
            current.remove(position);
            familyList.postValue(current);
            Log.d(TAG, "Contato removido - total agora: " + current.size());
        } else {
            Log.w(TAG, "Posição inválida ou lista vazia");
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "AddressFamilyViewModel destruído (onCleared)");
    }
}