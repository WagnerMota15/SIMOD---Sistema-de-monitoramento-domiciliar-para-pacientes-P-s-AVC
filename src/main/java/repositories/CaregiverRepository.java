package repositories;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.cuidador.Cuidador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaregiverRepository extends JpaRepository<Cuidador, UUID> {
}
