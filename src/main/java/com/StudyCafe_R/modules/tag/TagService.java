package com.StudyCafe_R.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
