package com.StudyCafe_R.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateNew(String title) {
        Tag tag = tagRepository.findByTitle(title)
                .orElseGet(() -> tagRepository.save(Tag.builder()
                        .title(title)
                        .build()));
        return tag;
    }

    public List<Tag> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags;
    }

    public Optional<Tag> findByTitle(String title) {
        Optional<Tag> byTitle = tagRepository.findByTitle(title);
        return byTitle;
    }
}
