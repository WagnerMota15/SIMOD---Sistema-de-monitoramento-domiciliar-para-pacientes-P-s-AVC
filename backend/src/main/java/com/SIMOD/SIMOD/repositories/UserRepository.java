package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
