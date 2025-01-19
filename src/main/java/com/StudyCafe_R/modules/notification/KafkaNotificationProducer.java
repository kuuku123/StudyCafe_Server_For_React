package com.StudyCafe_R.modules.notification;

import com.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;
    private final NotificationService notificationService;

    @Value("${kafka.topic.notification}")
    private String topic;

    public void sendNotification(Notification notification, Study study) {
        NotificationDto notificationDto = notificationService.getNotificationDto(notification, study);
        kafkaTemplate.send(topic, notificationDto);
        System.out.println("Sent notification: " + notification);
    }
}
