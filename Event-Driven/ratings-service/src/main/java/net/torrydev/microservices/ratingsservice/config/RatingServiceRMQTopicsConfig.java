package net.torrydev.microservices.ratingsservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rating-serv-topic")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RatingServiceRMQTopicsConfig {
    String daily;
    String exchange;
}
