package com.StudyCafe_R.modules.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class NotificationDto {
    private Long id;

    private String title;

    private String link;

    private String message;

    private boolean checked;

    private Long accountId;

    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}

