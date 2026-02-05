package com.example.simodapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.dto.RegisterRequest;
import com.example.simodapp.domain.enums.Role;

//classe de ViewModel responsável por amazenar o o Role selecionado pelo usuário antes dele ser enviado junto ao RegisterRequest
public class RoleViewModel extends ViewModel {

    // Armazena o role selecionado
    private final MutableLiveData<Role> selectedRole = new MutableLiveData<>();

    // Getter para observar o role selecionado
    public LiveData<Role> getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role role) {
        selectedRole.setValue(role);
    }

    // Retorna o role atual
    public Role getCurrentRole() {
        return selectedRole.getValue();
    }
}
