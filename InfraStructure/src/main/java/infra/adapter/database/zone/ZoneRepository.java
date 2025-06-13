package infra.adapter.database.zone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {

    Optional<ZoneEntity> findByCityAndProvince(String cityName, String provinceName);
}
