package com.StudyCafe_R.modules.notification;

import com.StudyCafe_R.modules.study.domain.Study;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    public void markAsRead(List<Notification> notifications) {
        notifications.forEach(notification -> notification.setChecked(true));
        notificationRepository.saveAll(notifications);
    }

    public NotificationDto getNotificationDto(Notification notification, Study study) {
        NotificationDto notificationDto = modelMapper.map(notification, NotificationDto.class);
        notificationDto.setAccountEmail(notification.getAccount().getEmail());
        notificationDto.setStudyPath(study.getEncodedPath());
        return notificationDto;
    }
}
