package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.usuario.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByCpf(String cpf);
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User usuario set usuario.fcmToken = null where usuario.fcmToken = :token")
    void limparToken(@Param("token") String token);
}
