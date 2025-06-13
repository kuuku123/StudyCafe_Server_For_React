package infra.adapter.database.tag;

import com.StudyCafe_R.domain.Tag;
import com.StudyCafe_R.usecase.port.db.TagPersistenceOperationsOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagPersistenceGateway implements TagPersistenceOperationsOutputPort {

    private final TagRepository tagRepository;

    @Override
    public void save(Tag tag) {

    }

    @Override
    public Optional<Tag> findById(Long accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<Tag>> findAllById(Set<Long> tagIds) {
        return Optional.empty();
    }
}
