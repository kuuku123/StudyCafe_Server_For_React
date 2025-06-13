package com.StudyCafe_R.usecase.port.db;

import com.StudyCafe_R.domain.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagPersistenceOperationsOutputPort {
    void save(Tag tag);

    Optional<Tag> findById(Long accountId);
    Optional<Set<Tag>> findAllById(Set<Long> tagIds);
}
