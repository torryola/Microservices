package net.torrydev.microservices.productsservice.repository;


import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class ProductTestContainerBase {
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest").withUsername("root")
            .withPassword("password")
            .withDatabaseName("testuser");
    static {
        mySQLContainer.start();
    }
}
