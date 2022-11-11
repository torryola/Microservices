package net.torrydev.microservices.commentsservice.service;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.commentsservice.config.CommentServiceRMQTopicsConfig;
import net.torrydev.microservices.commentsservice.model.RabbitMqMsg;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Publisher Impl - Generate List of Random Numbers of size num specified or @MIN_LIMIT if not
 * specified.
 * Process the List<Integer> and convert it to msgBody.
 * Publish the msg to RabbitMQ
 */
@Service
@Slf4j
public class RabbitMqPubServiceImpl {

    private static Integer MAX_NUMBER = 1000;
    private static final Integer MAX_LIMIT = 10;
    public static final Integer MIN_LIMIT = 10;
    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    CommentServiceRMQTopicsConfig rmqTopicsConfig;

    public String generateNum_And_PublishMsg(int num){
        RabbitMqMsg msg = buildMessage(num).orElse(RabbitMqMsg.builder().msgError("Error build message for request "+num).build());
        amqpTemplate.convertAndSend(rmqTopicsConfig.getExchange(), rmqTopicsConfig.getDaily(), msg);
        return msg.getMsgBody();
    }

    private Optional<RabbitMqMsg> buildMessage(int  num) {
        List<Integer> list = generateListOfNumbers(num);
        int total = list.stream().reduce(0, Integer::sum);
        String msg = getMsgBody(list.toString(), total);
        return Optional.ofNullable(RabbitMqMsg.builder()
                .listOfNumbers(list)
                .total(BigInteger.valueOf(total))
                .msgBody(msg).build());
    }

    private String generate_StringOf_RandomNumbers(int  num) {
        List<Integer> list = generateListOfNumbers(num);
        int total = list.stream().reduce(0, Integer::sum);
        return getMsgBody(list.toString(), total);
    }

    private List<Integer> generateListOfNumbers(int num) {
        // Generate List of Numbers and return as string
        List<Integer> list = new ArrayList<>();
        int limit = Math.min(MIN_LIMIT, num);
        for (int i = 0; i < limit; i++) {
            list.add(ThreadLocalRandom.current().nextInt(MAX_NUMBER));
        }
        return list;
    }

    private static String getMsgBody(String strList, int total){
        return String.format("Numbers generated are %s and Total of %d", strList, total);
    }

    //    // Every 12hr
//    @Scheduled(fixedRate = 12, timeUnit = TimeUnit.HOURS)
//    void twelveHourlyReport() {
//        log.info("Every 1 Minute =====> Publish to RabbitMq Here! " + LocalTime.now());
//    }
//
//    @Scheduled(cron = "@weekly")
////(cron = "* * * * * SAT")
//    void weeklyReport() {
//        log.info("Every 5 Minutes =====> Publish to RabbitMq Here! on Saturday " + LocalTime.now());
//    }
//
//    @Scheduled(cron = "@monthly")
//    void monthlyReport() {
//    }
}
