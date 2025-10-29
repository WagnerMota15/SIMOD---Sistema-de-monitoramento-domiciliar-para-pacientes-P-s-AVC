package repositories;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.familiares.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FamilyRepository extends JpaRepository<Family, UUID> {
}
