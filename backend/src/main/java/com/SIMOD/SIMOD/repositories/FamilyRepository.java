package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.familiares.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FamilyRepository extends JpaRepository<Family, UUID> {
}
