package com.StudyCafe_R.modules.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByTitle(String title);
}
