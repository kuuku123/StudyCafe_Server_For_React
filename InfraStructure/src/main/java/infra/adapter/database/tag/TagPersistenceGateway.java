package infra.adapter.database.tag;

import com.StudyCafe_R.domain.Tag;
import com.StudyCafe_R.usecase.port.db.TagPersistenceOperationsOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagPersistenceGateway implements TagPersistenceOperationsOutputPort {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public void save(Tag tag) {

    }

    @Override
    public Optional<Tag> findById(Long accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<Tag>> findAllById(Set<Long> tagIds) {
        // load all matching entities
        Set<Tag> tags = tagRepository
                .findAllById(tagIds)         // Iterable<TagEntity>
                .stream()
                .map(tagMapper::mapToDomain) // Tag
                .collect(Collectors.toSet());

        // if any IDs were missing, signal empty
        if (tags.size() != tagIds.size()) {
            return Optional.empty();
        }

        return Optional.of(tags);
    }
}
