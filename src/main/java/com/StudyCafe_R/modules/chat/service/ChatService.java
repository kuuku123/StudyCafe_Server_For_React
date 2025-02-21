package com.StudyCafe_R.modules.chat.service;


import com.StudyCafe_R.modules.chat.domain.Chat;
import com.StudyCafe_R.modules.chat.domain.ChatMessageDto;
import com.StudyCafe_R.modules.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;

    public void saveChat(ChatMessageDto chatMessageDto) {
        Chat chat = modelMapper.map(chatMessageDto, Chat.class);
        chatRepository.save(chat);
    }

    public List<ChatMessageDto> getAllChat(String path) {
        List<Chat> all = chatRepository.findChatsByStudyPath(path);
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
        for (Chat chat : all) {
            ChatMessageDto dto = modelMapper.map(chat, ChatMessageDto.class);
            chatMessageDtoList.add(dto);
        }
        return chatMessageDtoList;
    }
}
