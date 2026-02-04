package com.SIMOD.SIMOD.mapper.diario;

import com.SIMOD.SIMOD.domain.model.diario.HealthDiary;
import com.SIMOD.SIMOD.dto.diario.HealthDiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthDiaryMapper {

    private final HealthDiaryMedicineMapper medicineMapper;
    private final HealthDiaryDietMapper dietMapper;
    private final HealthDiaryActivityMapper activityMapper;
    private final HealthDiarySessionMapper sessionMapper;

    public HealthDiaryResponse toResponse(HealthDiary diary) {
        return new HealthDiaryResponse(
                diary.getId(),
                diary.getDiaryDate(),

                diary.getSystolicBp(),
                diary.getDiastolicBp(),
                diary.getHeartRate(),
                diary.getWeight(),
                diary.getSymptoms(),
                diary.getGlucose(),

                diary.getMedicines().stream()
                        .map(medicineMapper::toResponse)
                        .toList(),

                diary.getDiets().stream()
                        .map(dietMapper::toResponse)
                        .toList(),

                diary.getActivities().stream()
                        .map(activityMapper::toResponse)
                        .toList(),

                diary.getSessions().stream()
                        .map(sessionMapper::toResponse)
                        .toList(),

                diary.getCreatedAt(),
                diary.getPatient().getIdUser(),
                diary.getCaregiver() != null
                        ? diary.getCaregiver().getIdUser()
                        : null
        );
    }
}
