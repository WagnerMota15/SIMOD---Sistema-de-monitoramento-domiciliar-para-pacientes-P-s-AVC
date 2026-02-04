package com.SIMOD.SIMOD.dto.plansTreatment;

import com.SIMOD.SIMOD.domain.enums.Status;

import java.math.BigDecimal;
import java.util.UUID;

public record MedicinesResponse(
   UUID id,
   String name,
   BigDecimal dosage,
   String unity,
   Integer frequency,
   String description,
   Status status,
   UUID patient_id,
   UUID professional_id
) {}

