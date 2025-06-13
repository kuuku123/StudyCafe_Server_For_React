package com.StudyCafe_R.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Tag {

    private Long id;

    private String title;

    private Set<Long> accounts = new HashSet<>();

    private Set<Long> studies = new HashSet<>();

    @Builder
    public Tag(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
