package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = null;

        user = userRepository.findByCpf(login).orElse(null);

        if (user == null) {
            user = userRepository.findByEmail(login).orElse(null);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }
        return new UserDetailsImpl(user);
    }
}