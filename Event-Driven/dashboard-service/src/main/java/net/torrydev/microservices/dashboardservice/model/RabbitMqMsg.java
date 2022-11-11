package net.torrydev.microservices.dashboardservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Convert this to JPA Entity for Db-related ops
 * E.g.
 * 1 - Annotate this Entity and the fields accordingly
 * 2 - Create Repository
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RabbitMqMsg {
    @Builder.Default
    private String msgId = UUID.randomUUID().toString();
    private String msgBody;
    @Builder.Default
    private String msgDateTime = LocalDateTime.now().toString();
    private String msgError;
    private List<Integer> listOfNumbers;
    private BigInteger total;

}
