package repositories;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {
}
