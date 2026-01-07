package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.usuario.User;
import com.SIMOD.SIMOD.domain.usuario.UserRequest;
import com.SIMOD.SIMOD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public User criarUsuario(UserRequest dado){

        //criação do usuário com seus dados
        User novoUsuario = new User();
        novoUsuario.setCPF(dado.CPF());
        novoUsuario.setNomeCompleto(dado.nomeComplete());
        novoUsuario.setEmail(dado.email());
        novoUsuario.setPassword(dado.password());
        novoUsuario.setTelephone(dado.telephone());

        return userRepository.save(novoUsuario);

    }

}
