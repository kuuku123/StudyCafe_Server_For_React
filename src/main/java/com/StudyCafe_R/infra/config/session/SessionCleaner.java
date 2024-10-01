package com.StudyCafe_R.infra.config.session;


import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionCleaner implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    public SessionCleaner(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Define the pattern to match all session keys
        String sessionPattern = "spring:session:sessions:*";

        // Get all session keys
        var keys = redisTemplate.keys(sessionPattern);

        if (keys != null && !keys.isEmpty()) {
            // Delete all session keys from Redis
            redisTemplate.delete(keys);
            System.out.println("Cleared " + keys.size() + " sessions from Redis.");
        } else {
            System.out.println("No sessions found to clear.");
        }
    }
}
