package com.credit.simulator.consumer.infrastructure.worker;

import com.credit.simulator.consumer.application.usecase.CreditSimulationUseCase;
import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CreditSimulationCalculateWorker {

	private static final Logger logger = LoggerFactory.getLogger(CreditSimulationCalculateWorker.class);
	private final CreditSimulationUseCase calculateCreditUseCase;

	public CreditSimulationCalculateWorker(CreditSimulationUseCase calculateCreditUseCase) {
		this.calculateCreditUseCase = calculateCreditUseCase;
	}

	@Async("creditCalculationExecutor")
	public void processSimulation(CreditSimulation message) {
		try {
			logger.info("Processing credit simulation for ID: {}", message.getId());

			RateTypeEnum rateTypeEnum = RateTypeEnum.valueOf(message.getInterestRateType().toUpperCase());
			calculateCreditUseCase
					.calculateSimulation(message.getId(), message.getLoanAmount(), message.getBirthDate(),
							message.getPaymentTermMonths(), rateTypeEnum)
					.subscribe(
							result -> logger.info("Credit simulation processed successfully for ID: {}", message.getId()),
							error -> logger.error("Error processing credit simulation for ID: {}", message.getId(), error));

		} catch (InvalidCreditSimulationException e) {
			logger.error("Unexpected error processing simulation for ID: {}", message.getId(), e);
		}
	}
}