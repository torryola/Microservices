package net.torrydev.microservices.dashboardservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.username}")
    private String rmqUserName;
    @Value("${spring.rabbitmq.password}")
    private String rmqPassword;
    @Value("${spring.rabbitmq.host}")
    private String rmqHost;

    @Bean
    public MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ConnectionFactory
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(rmqUserName);
        connectionFactory.setPassword(rmqPassword);
        connectionFactory.setHost(rmqHost);
        return connectionFactory;
    }

    // Queue Listener
//    @Bean
//    public MessageListenerContainer messageListenerContainer() {
//        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();
//        messageListenerContainer.addQueues(appUserQueue());
//        messageListenerContainer.setConnectionFactory(connectionFactory());
//        messageListenerContainer.setMessageListener(new AppUser_Service_Consumer());
//        return messageListenerContainer;
//    }

//    @Bean
//    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(getMessageConverter());
//        return rabbitTemplate;
//    }
}
