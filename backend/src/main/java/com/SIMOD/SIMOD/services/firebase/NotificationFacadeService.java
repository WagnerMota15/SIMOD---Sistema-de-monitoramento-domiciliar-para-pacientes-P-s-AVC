package com.SIMOD.SIMOD.services.firebase;

import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.services.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationFacadeService {

    private final NotificationsService notificationsService;
    private final PushNotificationService pushNotificationService;

    public void notify(UUID userId, NotificationsRequest request) {
        notificationsService.criarNotificacao(userId, request);
        pushNotificationService.send(userId, request);
    }
}

