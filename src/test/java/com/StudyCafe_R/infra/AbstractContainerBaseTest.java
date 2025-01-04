package com.StudyCafe_R.infra;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {

//    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
//    private static final int REDIS_PORT = 16379;
//    private static final GenericContainer REDIS_CONTAINER;
//
//    static {
//        REDIS_CONTAINER = new GenericContainer(REDIS_IMAGE)
//                .withExposedPorts(REDIS_PORT)
//                .withReuse(true);
//        REDIS_CONTAINER.start();
//    }
//
//    @DynamicPropertySource
//    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
//        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT)
//                .toString());
//    }
}

