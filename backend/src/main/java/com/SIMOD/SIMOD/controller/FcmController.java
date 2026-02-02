package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.dto.auth.UpdateFcmTokenRequest;
import com.SIMOD.SIMOD.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Fcm")
@RequiredArgsConstructor
public class FcmController {

    private final AuthService authService;

    @PutMapping("/fcm-token")
    public ResponseEntity<Void> atualizarFcmToken(
            Authentication authentication,
            @RequestBody @Valid UpdateFcmTokenRequest request
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        authService.atualizarFcmToken(
                userDetails.getUser().getIdUser(),
                request.fcmToken(),
                request.platform()
        );

        return ResponseEntity.noContent().build();
    }
}
