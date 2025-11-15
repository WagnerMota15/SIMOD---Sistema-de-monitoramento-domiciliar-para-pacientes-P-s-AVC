package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.domain.usuario.User;
import com.SIMOD.SIMOD.domain.usuario.UserRequest;
import com.SIMOD.SIMOD.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")


public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRequest body){
        User usuario = this.userService.criarUsuario(body);
        return ResponseEntity.ok(usuario);

    }

}
