package com.SIMOD.SIMOD.mapper.diario;

import com.SIMOD.SIMOD.domain.model.diario.HealthDiarySession;
import com.SIMOD.SIMOD.dto.diario.HealthDiarySessionResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthDiarySessionMapper {

    public HealthDiarySessionResponse toResponse(HealthDiarySession session) {
        return new HealthDiarySessionResponse(
                session.getSession().getId(),
                session.getSession().getDateTime(),
                session.isAttended(),
                session.getNote()
        );
    }
}
