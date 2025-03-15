package com.StudyCafe_R.modules.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
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

    public void initTagData() throws IOException {
        if (tagRepository.count() == 0) {
            InputStream inputStream = new ClassPathResource("tags.csv").getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Split the line into individual words using comma as delimiter
                String[] words = line.split(",");
                for (String word : words) {
                    String trimmedWord = word.trim();
                    if (!trimmedWord.isEmpty()) {
                        Tag tag = Tag.builder()
                          .title(trimmedWord)
                          .build();
                        tagRepository.save(tag);
                    }
                }
            }
        }
    }
}
