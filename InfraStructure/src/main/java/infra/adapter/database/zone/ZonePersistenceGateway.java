package infra.adapter.database.zone;

import com.StudyCafe_R.domain.Zone;
import com.StudyCafe_R.usecase.port.db.ZonePersistenceOperationsOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ZonePersistenceGateway implements ZonePersistenceOperationsOutput {

    private final ZoneRepository zoneRepository;

    @Override
    public void save(Zone zone) {

    }

    @Override
    public Optional<Zone> findById(Long zoneId) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<Zone>> findAllById(Set<Long> zoneIds) {
        return Optional.empty();
    }
}
