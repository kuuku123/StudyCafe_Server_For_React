package infra.adapter.database.zone;


import com.StudyCafe_R.domain.Zone;
import org.springframework.stereotype.Component;

@Component
public class ZoneMapper {

    /**
     * Maps a domain Zone object to a new ZoneEntity object.
     * This is typically used when creating a new zone.
     * @param zone The domain object.
     * @return A new ZoneEntity.
     */
    public ZoneEntity mapToEntity(Zone zone) {
        return ZoneEntity.builder()
                .id(zone.getId()) // Can be null for new entities
                .city(zone.getCity())
                .localNameOfCity(zone.getLocalNameOfCity())
                .province(zone.getProvince())
                .build();
    }

    /**
     * Updates an existing ZoneEntity with data from a domain Zone object.
     * This is crucial for update operations to ensure you're working with a managed JPA entity.
     * @param entity The managed JPA entity to update.
     * @param domain The domain object with the new state.
     */
    public void updateEntityFromDomain(ZoneEntity entity, Zone domain) {
        entity.setCity(domain.getCity());
        entity.setLocalNameOfCity(domain.getLocalNameOfCity());
        entity.setProvince(domain.getProvince());
    }

    /**
     * Maps a persisted ZoneEntity back to a domain Zone object.
     * @param entity The JPA entity from the database.
     * @return A domain Zone object.
     */
    public Zone mapToDomain(ZoneEntity entity) {
        if (entity == null) {
            return null;
        }

        return Zone.builder()
                .id(entity.getId())
                .city(entity.getCity())
                .localNameOfCity(entity.getLocalNameOfCity())
                .province(entity.getProvince())
                .build();
    }
}