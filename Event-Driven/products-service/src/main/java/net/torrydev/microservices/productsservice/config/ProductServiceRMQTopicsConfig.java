package net.torrydev.microservices.productsservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "prod-serv-topic")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ProductServiceRMQTopicsConfig {
    String daily;
    String exchange;
}
