package com.StudyCafe_R.infra;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractContainerBaseTest {
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int CONTAINER_PORT = 6379;
    private static final int HOST_PORT = 16379;

    public static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(
            DockerImageName.parse(REDIS_IMAGE)
    )
            // 1) Expose the default Redis port inside the container
            .withExposedPorts(CONTAINER_PORT)

            // 2) Modify the container's host config to bind the container port 6379 to host port 16379
            .withCreateContainerCmdModifier(cmd -> {
                HostConfig hostConfig = cmd.getHostConfig();
                if (hostConfig == null) {
                    hostConfig = new HostConfig();
                    cmd.withHostConfig(hostConfig);
                }
                hostConfig
                        .withPortBindings(new PortBinding(
                                Ports.Binding.bindPort(HOST_PORT),
                                new ExposedPort(CONTAINER_PORT)
                        ));
            })
            .withReuse(true);

    static {
        REDIS_CONTAINER.start();
    }
}

