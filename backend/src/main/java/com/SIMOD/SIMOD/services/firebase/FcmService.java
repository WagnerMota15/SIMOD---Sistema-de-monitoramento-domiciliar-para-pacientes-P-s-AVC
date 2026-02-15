package com.SIMOD.SIMOD.services.firebase;

import com.SIMOD.SIMOD.repositories.UserDevicesRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final UserDevicesRepository userDevicesRepository;

    public void send(String token, String title, String body) {

        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            log.info("FCM enviado com sucesso");

        } catch (FirebaseMessagingException e) {

            log.warn("Falha ao enviar FCM para token {}", token);
            log.warn("Erro Firebase: {}", e.getErrorCode());

            if (e.getMessagingErrorCode() != null &&
                    e.getMessagingErrorCode().name().equals("UNREGISTERED")) {

                userDevicesRepository.deleteByFcmToken(token);
                log.warn("Token FCM removido: {}", token);
            }
        }
    }
}


