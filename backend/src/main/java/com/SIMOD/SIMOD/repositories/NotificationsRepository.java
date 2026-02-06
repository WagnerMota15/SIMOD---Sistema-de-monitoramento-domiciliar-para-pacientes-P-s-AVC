package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.mensagens.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.UUID;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {
    List<Notifications> findByUserId(UUID userId);

    Page<Notifications> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<Notifications> findByUserIdAndReadFalseOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    // Contagem de notificações não lidas
    long countByUserIdAndReadFalse(UUID userId);

    // Marca todas as notificações lidas de uma vez
    @Modifying
    @Transactional
    @Query("""
        UPDATE Notifications n
        SET n.read = true
        WHERE n.userId = :userId
          AND n.read = false
    """)
    int marcarTodasComoLidas(@Param("userId") UUID userId);
}
