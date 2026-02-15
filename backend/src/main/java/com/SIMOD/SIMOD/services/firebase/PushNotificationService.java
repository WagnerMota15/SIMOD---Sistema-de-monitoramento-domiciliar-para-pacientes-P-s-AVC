package com.SIMOD.SIMOD.services.firebase;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.domain.model.usuario.UserDevices;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.repositories.UserDevicesRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final UserDevicesRepository userDevicesRepository;

    public void send(UUID userId, NotificationsRequest request) {

        List<UserDevices> devices =
                userDevicesRepository.findByUserId(userId);

        for (UserDevices device : devices) {
            fcmService.send(
                    device.getFcmToken(),
                    request.titulo(),
                    request.mensagem()
            );
        }
    }

}

