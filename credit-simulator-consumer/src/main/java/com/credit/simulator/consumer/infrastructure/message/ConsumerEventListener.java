package com.credit.simulator.consumer.infrastructure.message;

import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.consumer.infrastructure.worker.CreditSimulationCalculateWorker;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerEventListener.class);
	private final CreditSimulationCalculateWorker worker;

	public ConsumerEventListener(CreditSimulationCalculateWorker worker, ObjectMapper objectMapper) {
		this.worker = worker;
	}

	@RabbitListener(queues = "${app.rabbitmq.queues.credit-simulation}")
	public void consume(CreditSimulation creditSimulatio) {
		try {
			logger.info("Received credit simulation message: {}", creditSimulatio.toString());

			worker.processSimulation(creditSimulatio);
			
		} catch (InvalidCreditSimulationException e) {
			logger.error("Error processing credit simulation message: {}", creditSimulatio.toString(), e);
		}
	}
}