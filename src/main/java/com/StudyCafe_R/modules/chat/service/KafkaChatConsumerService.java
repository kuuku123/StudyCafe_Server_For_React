package com.StudyCafe_R.modules.chat.service;


import com.StudyCafe_R.modules.chat.domain.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaChatConsumerService {

    private final ChatService chatService;

    @KafkaListener(topics = "${kafka.topic.chat}", groupId = "chat_group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ChatMessageDto chatMessage) {
        // Process the received chat message
        Thread thread = Thread.currentThread();
        System.out.println("Received chat message: " + chatMessage.getText() + " " + thread.getName());
        chatService.saveChat(chatMessage);
    }
}
