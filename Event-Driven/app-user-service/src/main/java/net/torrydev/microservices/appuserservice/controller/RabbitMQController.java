package net.torrydev.microservices.appuserservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.appuserservice.service.RabbitMqPubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.torrydev.microservices.appuserservice.constants.ServiceConstants.APP_USER_ENDPOINTS;

@Slf4j
@RestController
@RequestMapping(path = RabbitMQController.RABBIT_CONTROLLER_API)
public class RabbitMQController {

    public static final String RABBIT_CONTROLLER_API = APP_USER_ENDPOINTS + "/rmq";
    @Autowired
    RabbitMqPubServiceImpl rabbitMqService;

    @GetMapping("/generate")
    public String generateNumber() {
        return rabbitMqService.generateNum_And_PublishMsg(RabbitMqPubServiceImpl.MIN_LIMIT);
    }

    @GetMapping("/generate/{num}")
    public String generateNumber(@PathVariable("num") int num) {
        // Generate maximum of Ten numbers otherwise number of size num will be generated
        return rabbitMqService.generateNum_And_PublishMsg(num);
    }
}
