package net.torrydev.microservices.appuserservice.repository;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class TestContainerBaseClass {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withUsername("root")
            .withPassword("password")
            .withDatabaseName("testuser");
            //.withInitScript("classpath:test-data.sql");
    // Start container on class initialized
    static {
        mySQLContainer.start();
    }
}
