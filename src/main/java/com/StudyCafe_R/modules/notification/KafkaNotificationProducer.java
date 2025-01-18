package com.StudyCafe_R.modules.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    @Value("${kafka.topic.notification}")
    private String topic;

    public void sendNotification(Notification notification) {
        kafkaTemplate.send(topic, notification);
        System.out.println("Sent notification: " + notification);
    }
}
