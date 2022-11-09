package net.torrydev.microservices.commentsservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Object to send over to RabbitMQ server
 * Convert this to JPA Entity for Db-related ops
 * E.g.
 * 1 - Annotate this Entity and the fields according
 * 2 - Create Repository
 */
@Data @Builder
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
