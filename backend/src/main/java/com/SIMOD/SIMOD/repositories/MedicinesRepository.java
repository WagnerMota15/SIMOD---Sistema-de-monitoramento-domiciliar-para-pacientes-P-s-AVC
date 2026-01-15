package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicinesRepository extends JpaRepository<Medicines, UUID> {
}
