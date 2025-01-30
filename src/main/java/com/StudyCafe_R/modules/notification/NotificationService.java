package com.StudyCafe_R.modules.notification;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    public NotificationDto getNotificationDto(Notification notification, String studyPath) {
        NotificationDto notificationDto = modelMapper.map(notification, NotificationDto.class);
        notificationDto.setAccountEmail(notification.getAccount().getEmail());
        notificationDto.setStudyPath(studyPath);
        return notificationDto;
    }

    public boolean markAsChecked(Long id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setChecked(true);
                    notificationRepository.save(notification); // Save the updated notification
                    return true; // Indicate success
                })
                .orElse(false); // Return false if the notification was not found
    }


    public List<NotificationDto> getUnReadNotification(Account account ) {
        List<Notification> notifications = notificationRepository.findByAccountAndChecked(account,  false);
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            String studyPath = notification.getStudyPath();
            NotificationDto notificationDto = getNotificationDto(notification, studyPath);
            notificationDtos.add(notificationDto);
        }
        return notificationDtos;
    }
}
