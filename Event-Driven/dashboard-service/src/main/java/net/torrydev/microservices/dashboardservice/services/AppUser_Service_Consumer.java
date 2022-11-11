package net.torrydev.microservices.dashboardservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.dashboardservice.model.RabbitMqSubMsg;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static net.torrydev.microservices.dashboardservice.constant.RabbitMQ_Constant.MSG_ERROR;
import static net.torrydev.microservices.dashboardservice.constant.RabbitMQ_Constant.MSG_ID;

/**
 * Service for processing App-User published messages
 * For getting the message from RabbitMQ Queue
 */
@Slf4j
@Service
public class AppUser_Service_Consumer { // implements MessageListener {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ObjectMapper mapper;

//    @Override
    public void onMessage(Message message) {
        Map bodyMap;
        try {
            bodyMap = new ObjectMapper().readValue(message.getBody(), Map.class);
            // Send update back to the publisher Here
            bodyMap.keySet().forEach(k -> log.info("Key : "+k +" Value : "+bodyMap.get(k) ));
            /**
             * ========== Msg Body Sample =============
             * Key : msgId Value : 5bbed1dc-c6c7-48fe-a6b4-05bdaea1b577
             * Key : msgBody Value : Numbers generated are [926, 90, 532, 145, 293] and Total of 1986
             * Key : msgDateTime Value : 2022-10-16T12:40:20.735473700
             * Key : msgError Value : null
             * Key : listOfNumbers Value : [926, 90, 532, 145, 293]
             * Key : total Value : 1986
             */
            // Msg is processed Send update to Publisher
            String error = (String) bodyMap.get(MSG_ERROR);
            RabbitMqSubMsg processMsg = RabbitMqSubMsg.builder().msgId((String) bodyMap.get(MSG_ID)).isProcessed(error == null? Boolean.TRUE : Boolean.FALSE)
                    .msgDateTime(LocalDateTime.now().toString())
                    .msgError(error).build();
            log.info("Processed Msg to Send back to Publisher ========>>> {}", processMsg.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
