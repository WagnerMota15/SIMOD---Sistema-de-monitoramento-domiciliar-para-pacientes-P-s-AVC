package repositories;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.historicoMedico.Historical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistoricalRepository extends JpaRepository<Historical, UUID> {
}
