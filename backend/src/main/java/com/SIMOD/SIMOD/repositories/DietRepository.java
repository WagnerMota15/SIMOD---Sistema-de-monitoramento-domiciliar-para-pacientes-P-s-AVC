package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {
}
