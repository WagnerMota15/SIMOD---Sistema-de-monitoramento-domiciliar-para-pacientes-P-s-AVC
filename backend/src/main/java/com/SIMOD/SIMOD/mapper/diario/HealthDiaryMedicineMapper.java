package com.SIMOD.SIMOD.mapper.diario;

import com.SIMOD.SIMOD.domain.model.diario.HealthDiaryMedicine;
import com.SIMOD.SIMOD.dto.diario.HealthDiaryMedicineResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthDiaryMedicineMapper {

    public HealthDiaryMedicineResponse toResponse(HealthDiaryMedicine medicine) {
        return new HealthDiaryMedicineResponse(
                medicine.getMedicine().getIdMedicine(),
                medicine.getMedicine().getName(),
                medicine.isTaken(),
                medicine.getDoseTaken(),
                medicine.getUnityTaken(),
                medicine.getTimeTaken(),
                medicine.getNote()
        );
    }
}
