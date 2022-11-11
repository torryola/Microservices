package net.torrydev.microservices.appuserservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.appuserservice.model.RabbitMqSubMsg;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Subscriber - Receives msg from Consumer when message sent is processed.
 * Receive {msgId, isProcessed, dateProcessed, errorMsg} - to update e.g. DB or RabbitMQ
 *
 * To be implemented later for receiving reply from Consumer
 */
@Slf4j
public class RabbitMqSubServiceImpl { //implements MessageListener {
    // Sub-Exchange
    // Sub Queue
    // Sub RoutingKey
    @Autowired
    ObjectMapper mapper;

    void updateMessage(String msg){
        // Use ObjectMapper to map msg to an obj
        RabbitMqSubMsg subMsg = mapper.convertValue(msg, RabbitMqSubMsg.class);
        // Update Msg using msgId

    }

   // @Override
    public void onMessage(Message message) {
        // Receive the processed message back from Subscribers
      log.info(" Processed Message ==========> {}", message.toString());
    }
}
