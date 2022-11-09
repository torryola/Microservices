package net.torrydev.microservices.ratingsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RabbitMqSubMsg {
    private String msgId;
    private String msgDateTime;
    private String msgError;
    private boolean isProcessed;

}
