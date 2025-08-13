package com.credit.simulator.consumer.infrastructure.worker;

import com.credit.simulator.consumer.application.usecase.CreditSimulationUseCase;
import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.CreditSimulationResult;
import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.consumer.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditSimulationCalculateWorkerTest {

	@Mock
	private CreditSimulationUseCase calculateCreditUseCase;

	private CreditSimulationCalculateWorker worker;

	@BeforeEach
	void setUp() {
		worker = new CreditSimulationCalculateWorker(calculateCreditUseCase);
	}
	
	private CreditSimulation createCreditSimulation(String rateType) {
		return new CreditSimulation("689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, CreditSimulationStatusEnum.PENDING,
				null, null, null, null, rateType, CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now());
	}

	// Deve processar simulação com taxa fixa com sucesso
	@Test
	void processSimulationWithValidFixedRateSimulation() {
		CreditSimulation message = createCreditSimulation(RateTypeEnum.FIXED.toString());
		CreditSimulationResult expectedResult = new CreditSimulationResult(11000.0, 916.67, 1000.0, 0.06);

		when(calculateCreditUseCase.calculateSimulation(eq(message.getId()), eq(message.getLoanAmount()),
				eq(message.getBirthDate()), eq(message.getPaymentTermMonths()), eq(RateTypeEnum.FIXED)))
				.thenReturn(Mono.just(expectedResult));

		worker.processSimulation(message);

		verify(calculateCreditUseCase).calculateSimulation(message.getId(), message.getLoanAmount(),
				message.getBirthDate(), message.getPaymentTermMonths(), RateTypeEnum.FIXED);
	}

	// Deve processar simulação com taxa variável com sucesso
	@Test
	void processSimulationWithValidVariableRateSimulation() {
		CreditSimulation message = createCreditSimulation("VARIABLE");
		CreditSimulationResult expectedResult = new CreditSimulationResult(11200.0, 933.33, 1200.0, 0.08);

		when(calculateCreditUseCase.calculateSimulation(eq(message.getId()), eq(message.getLoanAmount()),
				eq(message.getBirthDate()), eq(message.getPaymentTermMonths()), eq(RateTypeEnum.VARIABLE)))
				.thenReturn(Mono.just(expectedResult));

		worker.processSimulation(message);

		verify(calculateCreditUseCase).calculateSimulation(message.getId(), message.getLoanAmount(),
				message.getBirthDate(), message.getPaymentTermMonths(), RateTypeEnum.VARIABLE);
	}


	// Deve processar simulação com valor alto de empréstimo
	@Test
	void processSimulationWithLargeLoanAmount() {
		CreditSimulation message = new CreditSimulation("689a25e86c29ff6d44f06af4", 1000000.0, LocalDate.of(1990, 1, 1), 60,
				CreditSimulationStatusEnum.PENDING, null, null, null, null, RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(),
				LocalDateTime.now());
		CreditSimulationResult expectedResult = new CreditSimulationResult(1100000.0, 18333.33, 100000.0, 0.06);

		when(calculateCreditUseCase.calculateSimulation(eq(message.getId()), eq(message.getLoanAmount()),
				eq(message.getBirthDate()), eq(message.getPaymentTermMonths()), eq(RateTypeEnum.FIXED)))
				.thenReturn(Mono.just(expectedResult));

		worker.processSimulation(message);

		verify(calculateCreditUseCase).calculateSimulation(message.getId(), message.getLoanAmount(),
				message.getBirthDate(), message.getPaymentTermMonths(), RateTypeEnum.FIXED);
	}
}