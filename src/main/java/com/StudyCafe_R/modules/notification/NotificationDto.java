package com.StudyCafe_R.modules.notification;

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

    private String accountEmail;

    private String studyPath;

    private NotificationType notificationType;
}

