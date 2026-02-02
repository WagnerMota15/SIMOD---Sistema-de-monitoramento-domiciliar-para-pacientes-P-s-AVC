package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.enums.Platform;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.domain.model.usuario.UserDevices;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;
import com.SIMOD.SIMOD.repositories.UserDevicesRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDevicesRepository userDevicesRepository;


    public UUID register(RegisterRequest request){

        User user = UserFactory.create(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return user.getIdUser();

    }

    @Transactional
    public void atualizarFcmToken(UUID userId, String fcmToken, Platform platform) {

        Optional<UserDevices> existing =
                userDevicesRepository.findByFcmToken(fcmToken);

        UserDevices device = existing.orElseGet(UserDevices::new);

        device.setUserId(userId);
        device.setFcmToken(fcmToken);
        device.setPlatform(platform);
        device.setLastLogin(LocalDateTime.now());

        if (device.getCreatedAt() == null) {
            device.setCreatedAt(LocalDateTime.now());
        }

        userDevicesRepository.save(device);
    }
}
