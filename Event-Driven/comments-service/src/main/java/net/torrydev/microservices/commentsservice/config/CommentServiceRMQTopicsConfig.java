package net.torrydev.microservices.commentsservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "comment-serv-topic")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CommentServiceRMQTopicsConfig {
    String daily;
    String exchange;
}
