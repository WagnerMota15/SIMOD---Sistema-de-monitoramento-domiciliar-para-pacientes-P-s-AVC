package com.SIMOD.SIMOD.mapper.diario;

import com.SIMOD.SIMOD.domain.model.diario.HealthDiaryActivity;
import com.SIMOD.SIMOD.dto.diario.HealthDiaryActivityResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthDiaryActivityMapper {

    public HealthDiaryActivityResponse toResponse(HealthDiaryActivity activity) {
        return new HealthDiaryActivityResponse(
                activity.getActivity().getId(),
                activity.getActivity().getName(),
                activity.getActivity().getType(),
                activity.isCompleted(),
                activity.getNote()
        );
    }
}
