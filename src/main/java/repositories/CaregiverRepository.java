package repositories;

import com.SIMOD.SIMOD.domain.cuidador.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaregiverRepository extends JpaRepository<Caregiver, UUID> {
}
