package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;
import com.SIMOD.SIMOD.repositories.CaregiverRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;


    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterRequest request){

        User user = UserFactory.create(request);
        userRepository.save(user);

    }
}
