package com.SIMOD.SIMOD.domain.model.diario;

import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "health_diary_diet",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"diary_id", "diet_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthDiaryDiet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diary_id")
    private HealthDiary diary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diet_id")
    private Diet diet;

    @Column(name = "followed")
    private boolean followed;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
