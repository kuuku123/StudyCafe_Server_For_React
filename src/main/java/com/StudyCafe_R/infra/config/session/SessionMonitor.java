package com.StudyCafe_R.infra.config.session;

import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class SessionMonitor {

    private final SessionRepository sessionRepository;
    private final RedisTemplate redisTemplate;

    @GetMapping("/sessions")
    public String getSessions() {
        Set<String> sessionKeys = redisTemplate.keys("spring:session:sessions*");
        List<Session> sessions = new ArrayList<>();
        for (String sessionKey : sessionKeys) {
            // Extract the session ID by removing the "spring:session:sessions:" prefix
            String sessionId = sessionKey.replace("spring:session:sessions:", "");
            Session session = sessionRepository.findById(sessionId);
            System.out.println("sessionKey = " + sessionKey);
            System.out.println("session = " + session);
            sessions.add(session);
        }
        return "hi";
    }


}
