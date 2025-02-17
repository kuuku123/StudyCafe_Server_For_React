package com.StudyCafe_R.modules.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Chat {

    @Id
    private Long Id;

    private String nickname;
    private String email;
    private String chat;
}
