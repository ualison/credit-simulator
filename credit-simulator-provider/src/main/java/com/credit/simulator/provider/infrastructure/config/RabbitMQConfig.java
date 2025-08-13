package com.credit.simulator.provider.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRabbit
@EnableRetry
public class RabbitMQConfig {

	@Value("${app.rabbitmq.queues.credit-simulation:credit-simulation-queue}")
	private String creditSimulationQueue;

    public static final String CREDIT_EXCHANGE = "credit.exchange";
    public static final String CREDIT_SIMULATION_ROUTING_KEY = "credit.simulation";
    public static final String CREDIT_RESULT_ROUTING_KEY = "credit.result";

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setMandatory(true);
        return template;
    }

    @Bean
    TopicExchange creditExchange() {
        return new TopicExchange(CREDIT_EXCHANGE);
    }

    @Bean
    Queue creditSimulationQueue() {
        return QueueBuilder.durable(creditSimulationQueue)
                .withArgument("x-message-ttl", 300000)
                .build();
    }

    @Bean
    Binding creditSimulationBinding() {
        return BindingBuilder.bind(creditSimulationQueue())
                .to(creditExchange())
                .with(CREDIT_SIMULATION_ROUTING_KEY);
    }
}