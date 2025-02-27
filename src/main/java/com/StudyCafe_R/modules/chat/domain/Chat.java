package com.StudyCafe_R.modules.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_study_path", columnList = "studyPath")
})
public class Chat {

    @Id
    private String id;
    private String studyPath;
    private String nickname;
    private String email;
    private String text;
    private LocalDateTime createdAt;
}
