package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;
import com.SIMOD.SIMOD.domain.model.mensagens.Notifications;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.Messages.NotificationsResponse;
import com.SIMOD.SIMOD.repositories.NotificationsRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final NotificationsRepository notificationRepository;

    @Transactional
    public void criarNotificacao(UUID userDestination, NotificationsRequest request) {
        Notifications notificacao = Notifications.builder()
                .userId(userDestination)
                .title(request.titulo())
                .message(request.mensagem())
                .type(TipoNotificacao.valueOf(request.tipo()))
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notificacao);
    }

    @Transactional(readOnly = true)
    public Page<NotificationsResponse> listarNotificacoes(Pageable pageable) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(usuarioLogadoId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationsResponse> listarNaoLidas(Pageable pageable) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        return notificationRepository
                .findByUserIdAndReadFalseOrderByCreatedAtDesc(usuarioLogadoId, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public void marcarComoLida(UUID notificacaoId) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        Notifications notificacao = notificationRepository.findById(notificacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        if (!notificacao.getUserId().equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode alterar notificações de outro usuário"
            );
        }

        notificacao.setRead(true);
        notificationRepository.save(notificacao);
    }

    @Transactional
    public void marcarTodasComoLidas() {
        UUID usuarioLogadoId = getUsuarioLogadoId();
        notificationRepository.marcarTodasComoLidas(usuarioLogadoId);
    }

    @Transactional(readOnly = true)
    public long contarNaoLidas() {
        UUID usuarioLogadoId = getUsuarioLogadoId();
        return notificationRepository.countByUserIdAndReadFalse(usuarioLogadoId);
    }

    @Transactional
    public void apagarNotificacao(UUID notificacaoId) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        Notifications notificacao = notificationRepository.findById(notificacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        if (!notificacao.getUserId().equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode apagar notificações de outro usuário"
            );
        }

        notificationRepository.delete(notificacao);
    }

    // Auxiliares
    private UUID getUsuarioLogadoId() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();

        var userDetails = (com.SIMOD.SIMOD.config.UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUser().getIdUser();
    }

    private NotificationsResponse toResponse(Notifications n) {
        return new NotificationsResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getType(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
