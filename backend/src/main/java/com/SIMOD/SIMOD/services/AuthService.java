package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;
import com.SIMOD.SIMOD.repositories.CaregiverRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID register(RegisterRequest request){

        User user = UserFactory.create(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return user.getIdUser();

    }
}
