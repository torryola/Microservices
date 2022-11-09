package net.torrydev.microservices.appuserservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RabbitMqSubMsg {
    private String msgId;
    private String msgDateTime;
    private String msgError;
    private boolean isProcessed;

}
