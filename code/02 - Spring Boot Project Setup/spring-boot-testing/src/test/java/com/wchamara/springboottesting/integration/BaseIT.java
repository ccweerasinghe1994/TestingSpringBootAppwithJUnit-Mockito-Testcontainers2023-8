package com.wchamara.springboottesting.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseIT {
    protected final static MySQLContainer<?> mySQLContainer;
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(EmployeeControllerIT.class);
        mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"))
                .withPassword("example")
                .withUsername("root")
                .withDatabaseName("ems")
                .withLogConsumer(new Slf4jLogConsumer(logger));
        mySQLContainer.start();
    }


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }
}
