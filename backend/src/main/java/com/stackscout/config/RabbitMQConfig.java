// File: RabbitMQConfig.java
package com.stackscout.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SCAN_QUEUE = "scan_queue";
    public static final String SCAN_EXCHANGE = "scan_exchange";
    public static final String SCAN_ROUTING_KEY = "scan_routing_key";

    @Bean
    public Queue scanQueue() {
        return new Queue(SCAN_QUEUE);
    }

    @Bean
    public TopicExchange scanExchange() {
        return new TopicExchange(SCAN_EXCHANGE);
    }

    @Bean
    public Binding scanBinding(Queue scanQueue, TopicExchange scanExchange) {
        return BindingBuilder.bind(scanQueue).to(scanExchange).with(SCAN_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
