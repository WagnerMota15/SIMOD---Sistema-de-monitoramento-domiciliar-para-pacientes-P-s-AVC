package com.SIMOD.SIMOD.domain.model.diario;

import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "health_diary_session",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"diary_id", "session_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthDiarySession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diary_id")
    private HealthDiary diary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private Sessions session;

    @Column(name = "attended")
    private boolean attended;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
