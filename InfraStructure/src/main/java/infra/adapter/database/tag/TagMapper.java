package infra.adapter.database.tag;


import com.StudyCafe_R.domain.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    /**
     * Maps a domain Tag object to a new TagEntity object.
     * This is typically used when creating a new tag.
     * @param tag The domain object.
     * @return A new TagEntity.
     */
    public TagEntity mapToEntity(Tag tag) {
        return TagEntity.builder()
                .id(tag.getId()) // Can be null for new entities
                .title(tag.getTitle())
                .build();
    }

    /**
     * Updates an existing TagEntity with data from a domain Tag object.
     * This is crucial for update operations to ensure you're working with a managed JPA entity.
     * @param entity The managed JPA entity to update.
     * @param domain The domain object with the new state.
     */
    public void updateEntityFromDomain(TagEntity entity, Tag domain) {
        entity.setTitle(domain.getTitle());
    }

    /**
     * Maps a persisted TagEntity back to a domain Tag object.
     * @param entity The JPA entity from the database.
     * @return A domain Tag object.
     */
    public Tag mapToDomain(TagEntity entity) {
        if (entity == null) {
            return null;
        }

        return Tag.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .build();
    }
}