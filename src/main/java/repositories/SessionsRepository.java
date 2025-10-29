package repositories;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.sessoes.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionsRepository extends JpaRepository<Sessions, UUID> {
}
