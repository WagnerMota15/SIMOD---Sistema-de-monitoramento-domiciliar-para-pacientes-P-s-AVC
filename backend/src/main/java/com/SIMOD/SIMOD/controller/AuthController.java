package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.config.JwtUtil;
import com.SIMOD.SIMOD.dto.auth.LoginRequest;
import com.SIMOD.SIMOD.dto.auth.LoginResponse;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;
import com.SIMOD.SIMOD.dto.auth.RegisterResponse;
import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.dto.familia.FamilyRequest;
import com.SIMOD.SIMOD.services.AddressService;
import com.SIMOD.SIMOD.services.AuthService;
import com.SIMOD.SIMOD.services.FamilyService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AddressService addressService;
    private final FamilyService familyService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthService authService, AddressService addressService, FamilyService familyService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authService = authService;
        this.addressService = addressService;
        this.familyService = familyService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {

        UUID userId = authService.register(request);

        // Autentica automaticamente após o registro do usuário
        //lógica reaproveitada do login
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.cpf(),
                        request.password()
                )
        );

        String jwt = jwtUtil.generateToken(authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse(userId, jwt));
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
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "CPF/Email ou senha inválidos"));
        }
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<Void> addAddress(
            @PathVariable UUID id,
            @RequestBody AddressRequest request) {

        addressService.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/family")
    public ResponseEntity<Void> addFamily(
            @PathVariable UUID id,
            @RequestBody @Valid List<FamilyRequest> request) {

        familyService.createContacstFamily(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
