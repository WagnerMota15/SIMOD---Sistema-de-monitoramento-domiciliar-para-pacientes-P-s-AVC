package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivitiesRepository extends JpaRepository<Activities, UUID> {
}
