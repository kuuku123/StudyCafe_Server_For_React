package com.StudyCafe_R.modules.chat.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto {
    private String id;
    private String studyPath;
    private String email;
    private String text;
    private LocalDateTime createdAt;
}
