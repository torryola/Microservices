package net.torrydev.microservices.appuserservice.config;

import net.torrydev.microservices.appuserservice.service.RabbitMqSubServiceImpl;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${app-user-rmq.queue-name}")
    private String queueName;
    @Value("${app-user-rmq.topic-exchange}")
    private String topicExchange;
    @Value("${app-user-rmq.topic-routing-key}")
    private String topicRoutingkey;
    @Value("${spring.rabbitmq.username}")
    private String rmqUserName;
    @Value("${spring.rabbitmq.password}")
    private String rmqPassword;
    @Value("${spring.rabbitmq.host}")
    private String rmqHost;

    @Bean
    public Queue getQueue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchange);
    }

    @Bean(name = "topicBindingBean")
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(topicRoutingkey);
    }

    // Message Obj Converter
    @Bean
    public MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(getMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(rmqUserName);
        connectionFactory.setPassword(rmqPassword);
        connectionFactory.setHost(rmqHost);
        return connectionFactory;
    }

    // Enable App to connect and create exchange and queue on startup
    @Bean
    ApplicationRunner applicationRunner(ConnectionFactory connectionFactory){
        return args -> connectionFactory.createConnection().close();
    }
}
