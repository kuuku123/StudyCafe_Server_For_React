package com.StudyCafe_R.modules.chat.controller;


import com.StudyCafe_R.infra.config.converter.LocalDateTimeAdapter;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.chat.domain.ChatMessageDto;
import com.StudyCafe_R.modules.chat.service.ChatService;
import com.StudyCafe_R.modules.notification.NotificationDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    @GetMapping("/get-all-chats/{path}")
    public ResponseEntity<String> getAllChats(@PathVariable String path) {
        List<ChatMessageDto> allChat = chatService.getAllChat(path);
        ApiResponse<List<ChatMessageDto>> apiResponse = new ApiResponse<>("get all chat", HttpStatus.OK, allChat);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }
}
