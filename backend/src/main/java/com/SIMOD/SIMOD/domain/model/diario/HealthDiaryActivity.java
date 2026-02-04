package com.SIMOD.SIMOD.domain.model.diario;

import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "health_diary_activity",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"diary_id", "activity_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthDiaryActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diary_id")
    private HealthDiary diary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id")
    private Activities activity;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
