package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.config.JwtUtil;
import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.dto.auth.*;
import com.SIMOD.SIMOD.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        UUID userId = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.login(),
                            request.password()
                    )
            );
            // Se chegou aqui → credenciais corretas
            String jwt = jwtUtil.generateToken(authentication);
            if (request.fcmToken() != null && !request.fcmToken().isBlank()) {
                UserDetailsImpl userDetails =
                        (UserDetailsImpl) authentication.getPrincipal();

                authService.atualizarFcmToken(
                        userDetails.getUser().getIdUser(),
                        request.fcmToken()
                );
            }
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "CPF/Email ou senha inválidos"));
        }
    }

    @PutMapping("/atualizar-senha")
    public ResponseEntity<Void> atualizarSenha(
            @RequestBody UpdatePasswordByEmailRequest request) {
        System.out.println("Entrei no controller");
        authService.atualizarSenhaPorEmail(
                request.email(),
                request.novaSenha()
        );

        return ResponseEntity.noContent().build();
    }
}
