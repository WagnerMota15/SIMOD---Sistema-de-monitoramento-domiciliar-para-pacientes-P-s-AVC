package repositories;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivitiesRepository extends JpaRepository<Activities, UUID> {
}
