package com.credit.simulator.provider.infrastructure.messaging;

import com.credit.simulator.provider.application.messagin.ProviderEventPublisher;
import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.provider.infrastructure.config.RabbitMQConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProviderEventPublisherImpl implements ProviderEventPublisher {

	private static final Logger logger = LoggerFactory.getLogger(ProviderEventPublisherImpl.class);
	private final RabbitTemplate rabbitTemplate;

	public ProviderEventPublisherImpl(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public Mono<Void> publishEvent(CreditSimulation simulation) {
		return Mono.fromRunnable(() -> {
			try {
				logger.info("Publishing credit simulation: {}", simulation.getId());
				rabbitTemplate.convertAndSend(RabbitMQConfig.CREDIT_EXCHANGE,
						RabbitMQConfig.CREDIT_SIMULATION_ROUTING_KEY, simulation);
				
				logger.info("Credit simulation published successfully: {}", simulation.getId());
			} catch (InvalidCreditSimulationException e) {
				logger.error("Error publishing credit simulation: {}", simulation.getId(), e);
				throw new RuntimeException("Failed to publish credit simulation", e);
			}
		});
	}
}