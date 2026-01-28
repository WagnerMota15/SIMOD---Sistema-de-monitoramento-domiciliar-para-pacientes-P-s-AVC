package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.model.mensagens.Alert;
import com.SIMOD.SIMOD.domain.model.mensagens.Reminders;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.Messages.NotificationsResponse;
import com.SIMOD.SIMOD.dto.Messages.ReminderRequest;
import com.SIMOD.SIMOD.dto.Messages.ReminderResponse;
import com.SIMOD.SIMOD.services.AlertService;
import com.SIMOD.SIMOD.services.NotificationsService;
import com.SIMOD.SIMOD.services.RemindersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MessagesController {

    private final RemindersService remindersService;
    private final NotificationsService notificationsService;
    private final AlertService alertService;

    // ----- Reminders ------
    @PostMapping("/lembretes/criar-lembrete")
    public ResponseEntity<Void> criarLembrete(
            @RequestBody @Valid ReminderRequest request,
            Authentication authentication
    ) {
        User usuario = getUsuario(authentication);
        remindersService.criarLembrete(usuario.getIdUser(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/lembretes/listar-lembretes")
    public ResponseEntity<List<ReminderResponse>> listarLembretes(Authentication authentication) {
        User usuario = getUsuario(authentication);
        return ResponseEntity.ok(
                remindersService.listarLembretes(usuario.getIdUser())
        );
    }

    @PutMapping("/lembretes/{lembreteId}/desativar")
    public ResponseEntity<Void> desativarLembrete(
            @PathVariable UUID lembreteId,
            Authentication authentication
    ) {
        User usuario = getUsuario(authentication);
        remindersService.desativarLembrete(usuario.getIdUser(), lembreteId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lembretes/{lembreteId}/reativar")
    public ResponseEntity<Void> reativarLembrete(@PathVariable UUID lembreteId) {
        remindersService.reativarLembrete(lembreteId);
        return ResponseEntity.noContent().build();
    }


    // ----- Notifications ------
    @GetMapping("/notificacoes/listar-notificacoes")
    public ResponseEntity<Page<NotificationsResponse>> listarNotificacoes(Pageable pageable) {
        return ResponseEntity.ok(
                notificationsService.listarNotificacoes(pageable)
        );
    }

    @PutMapping("/notificacoes/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable UUID id) {
        notificationsService.marcarComoLida(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/notificacoes/marcar-todas-lidas")
    public ResponseEntity<Void> marcarTodasComoLidas() {
        notificationsService.marcarTodasComoLidas();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notificacoes/nao-lidas")
    public ResponseEntity<Page<NotificationsResponse>> listarNaoLidas(Pageable pageable) {
        return ResponseEntity.ok(notificationsService.listarNaoLidas(pageable));
    }

    @GetMapping("/notificacoes/nao-lidas/contador")
    public ResponseEntity<Long> contadorNaoLidas() {
        return ResponseEntity.ok(
                notificationsService.contarNaoLidas()
        );
    }

    @DeleteMapping("/notificacoes/apagar/{id}")
    public ResponseEntity<Void> apagarNotificacao(@PathVariable UUID id) {
        notificationsService.apagarNotificacao(id);
        return ResponseEntity.noContent().build();
    }


    // ----- Alerts ------
    @GetMapping("/alertas")
    public ResponseEntity<List<Alert>> listarAlertas(Authentication authentication) {
        User usuario = getUsuario(authentication);
        return ResponseEntity.ok(
                alertService.listarAlertasPaciente(usuario.getIdUser())
        );
    }

    @PutMapping("/alertas/{alertId}/resolver")
    public ResponseEntity<Void> resolverAlerta(
            @PathVariable UUID alertId,
            Authentication authentication
    ) {
        User usuario = getUsuario(authentication);
        alertService.resolverAlerta(usuario.getIdUser(), alertId);
        return ResponseEntity.noContent().build();
    }


    // Auxiliar
    private User getUsuario(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
