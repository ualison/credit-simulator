package com.credit.simulator.consumer.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
@EnableRabbit
@EnableRetry
public class RabbitMQConfig {

	@Value("${app.rabbitmq.queues.credit-simulation:credit-simulation-queue}")
	private String creditSimulationQueue;

    public static final String CREDIT_EXCHANGE = "credit.exchange";
    public static final String CREDIT_SIMULATION_ROUTING_KEY = "credit.simulation";

    @Bean
   MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
   SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
   RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    TopicExchange creditExchange() {
        return new TopicExchange(CREDIT_EXCHANGE);
    }

    @Bean
    Queue creditSimulationQueueBean() {
        return QueueBuilder.durable(creditSimulationQueue)
                .withArgument("x-message-ttl", 300000)
                .build();
    }


    @Bean
    Binding creditSimulationBinding() {
        return BindingBuilder.bind(creditSimulationQueueBean())
                .to(creditExchange())
                .with(CREDIT_SIMULATION_ROUTING_KEY);
    }
}