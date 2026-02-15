package com.SIMOD.SIMOD.domain.model.diario;

import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(
        name = "health_diary_medicine",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"diary_id", "medicine_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthDiaryMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diary_id")
    private HealthDiary diary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id")
    private Medicines medicine;

    @Column(name = "taken")
    private boolean taken;

    @Column(name = "dose_taken", precision = 5, scale = 2)
    private BigDecimal doseTaken;

    @Column(name = "unity_taken", length = 10)
    private String unityTaken;

    @Column(name = "time_taken")
    private LocalTime timeTaken;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}

