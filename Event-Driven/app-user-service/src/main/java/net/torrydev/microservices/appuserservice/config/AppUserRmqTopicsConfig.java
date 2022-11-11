package net.torrydev.microservices.appuserservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app-user-topic")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AppUserRmqTopicsConfig {
    String daily;
}
