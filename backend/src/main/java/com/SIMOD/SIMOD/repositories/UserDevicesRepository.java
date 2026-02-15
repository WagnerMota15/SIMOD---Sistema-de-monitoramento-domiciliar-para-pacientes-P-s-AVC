package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.usuario.UserDevices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDevicesRepository extends JpaRepository<UserDevices, UUID> {
    List<UserDevices> findByUserId(UUID userId);

    Optional<UserDevices> findByFcmToken(String fcmToken);

    /* Remove o registro do dispositivo associado ao token informado, quando ocorre logout ou desregistro do dispositivo*/
    void deleteByFcmToken(String fcmToken);
}

