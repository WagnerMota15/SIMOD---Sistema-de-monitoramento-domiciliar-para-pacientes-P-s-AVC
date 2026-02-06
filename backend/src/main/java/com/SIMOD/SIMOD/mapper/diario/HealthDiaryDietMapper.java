package com.SIMOD.SIMOD.mapper.diario;

import com.SIMOD.SIMOD.domain.model.diario.HealthDiaryDiet;
import com.SIMOD.SIMOD.dto.diario.HealthDiaryDietResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthDiaryDietMapper {

    public HealthDiaryDietResponse toResponse(HealthDiaryDiet diet) {
        return new HealthDiaryDietResponse(
                diet.getDiet().getIdDiet(),
                diet.isFollowed(),
                diet.getNote()
        );
    }
}
