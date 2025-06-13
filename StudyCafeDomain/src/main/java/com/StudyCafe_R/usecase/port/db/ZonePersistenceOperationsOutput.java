package com.StudyCafe_R.usecase.port.db;

import com.StudyCafe_R.domain.Zone;

import java.util.Optional;
import java.util.Set;


public interface ZonePersistenceOperationsOutput {

    void save(Zone zone);

    Optional<Zone> findById(Long zoneId);

    Optional<Set<Zone>> findAllById(Set<Long> zoneIds);
}
