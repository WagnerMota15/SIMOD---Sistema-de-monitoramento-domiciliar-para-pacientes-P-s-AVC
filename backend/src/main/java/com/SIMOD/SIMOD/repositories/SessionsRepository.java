package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionsRepository extends JpaRepository<Sessions, UUID> {
}
