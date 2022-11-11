package net.torrydev.microservices.dashboardservice.services;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.dashboardservice.model.RabbitMqMsg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Service for processing App-User published messages
 * For getting the message from RabbitMQ Queue
 */
@Slf4j
@Component
public class Product_Service_Consumer {

    @RabbitListener(queues = {"${product-rmq.queue-name:product-pub}"})
    public void onMessage(RabbitMqMsg message) {
        log.info("========= Product_Service_Consumer ==========");
        log.info("=========RabbitMqMsg ========== {} ", message.toString());
    }
}
