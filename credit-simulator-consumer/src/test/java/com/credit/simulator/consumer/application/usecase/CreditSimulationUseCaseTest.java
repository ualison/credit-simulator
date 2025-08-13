package com.credit.simulator.consumer.application.usecase;

import com.credit.simulator.consumer.application.factory.RateStrategyFactory;
import com.credit.simulator.consumer.domain.model.*;
import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.consumer.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.domain.repository.CreditSimulationRepository;
import com.credit.simulator.consumer.domain.service.CreditCalculationService;
import com.credit.simulator.consumer.domain.strategy.RateStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreditSimulationUseCaseTest {

    @Mock
    private CreditSimulationRepository repository;

    @Mock
    private CreditCalculationService calculationService;

    @Mock
    private java.util.function.Function<CreditSimulationParameters, CreditSimulationResult> calculatePaymentFunction;

    @Mock
    private RateStrategyFactory rateStrategyFactory;

    @Mock
    private RateStrategy rateStrategy;

    private CreditSimulationUseCase useCase;

    @BeforeEach
    void setUp() {
        calculationService.calculatePayment = calculatePaymentFunction;
        useCase = new CreditSimulationUseCase(repository, calculationService, rateStrategyFactory);
    }

    private CreditSimulation createCreditSimulation(String id, Double loanAmount, LocalDate birthDate,
                                                    Integer paymentTermMonths, RateTypeEnum rateTypeEnum) {
        return new CreditSimulation(id, loanAmount, birthDate, paymentTermMonths, CreditSimulationStatusEnum.PENDING, null,
                null, null, null, rateTypeEnum.name(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(),
                LocalDateTime.now());
    }

    // Verifica se a simulação é calculada com sucesso usando parâmetros válidos
    @Test
    void simulationValidParameters() {
        String simulationId = "689a25e86c29ff6d44f06af4";
        Double loanAmount = 10000.0;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Integer paymentTermMonths = 12;
        RateTypeEnum rateType = RateTypeEnum.FIXED;
        Double annualRate = 0.06;

        CreditSimulationResult expectedResult = new CreditSimulationResult(11000.0, 916.67, 1000.0, annualRate);
        CreditSimulation existingSimulation = createCreditSimulation(simulationId, loanAmount, birthDate,
                paymentTermMonths, rateType);

        when(rateStrategyFactory.getRate(rateType)).thenReturn(rateStrategy);
        when(rateStrategy.getRate(birthDate)).thenReturn(annualRate);
        when(calculatePaymentFunction.apply(any())).thenReturn(expectedResult);
        when(repository.findById(simulationId)).thenReturn(Mono.just(existingSimulation));
        when(repository.update(any(CreditSimulation.class))).thenReturn(Mono.just(existingSimulation));

        Mono<CreditSimulationResult> result = useCase.calculateSimulation(simulationId, loanAmount, birthDate,
                paymentTermMonths, rateType);

        StepVerifier.create(result).expectNext(expectedResult).verifyComplete();

        verify(rateStrategyFactory).getRate(rateType);
        verify(rateStrategy).getRate(birthDate);
    }

    // Verifica que o cálculo continua mesmo se a atualização no repositório falhar
    @Test
    void resultRepositoryUpdateFails() {
        String simulationId = "689a25e86c29ff6d44f06af4";
        Double loanAmount = 10000.0;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Integer paymentTermMonths = 12;
        RateTypeEnum rateType = RateTypeEnum.FIXED;
        Double annualRate = 0.06;

        CreditSimulation existingSimulation = createCreditSimulation(simulationId, loanAmount, birthDate,
                paymentTermMonths, rateType);
        CreditSimulationResult expectedResult = new CreditSimulationResult(11000.0, 916.67, 1000.0, annualRate);

        when(rateStrategyFactory.getRate(rateType)).thenReturn(rateStrategy);
        when(rateStrategy.getRate(birthDate)).thenReturn(annualRate);
        when(calculationService.calculatePayment.apply(any())).thenReturn(expectedResult);
        when(repository.findById(simulationId)).thenReturn(Mono.just(existingSimulation));
        when(repository.update(any(CreditSimulation.class)))
                .thenReturn(Mono.error(new RuntimeException("Falha")));

        Mono<CreditSimulationResult> result = useCase.calculateSimulation(simulationId, loanAmount, birthDate,
                paymentTermMonths, rateType);

        StepVerifier.create(result).expectNext(expectedResult).verifyComplete();

        verify(repository).findById(simulationId);
    }

    // Testa se a estratégia de taxa variável é utilizada corretamente
    @Test
    void variableRateStrategyCorrect() {
        String simulationId = "689a25e86c29ff6d44f06af4";
        Double loanAmount = 10000.0;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Integer paymentTermMonths = 12;
        RateTypeEnum rateType = RateTypeEnum.VARIABLE;
        Double annualRate = 0.08;

        CreditSimulationResult expectedResult = new CreditSimulationResult(11200.0, 933.33, 1200.0, annualRate);

        when(rateStrategyFactory.getRate(rateType)).thenReturn(rateStrategy);
        when(rateStrategy.getRate(birthDate)).thenReturn(annualRate);
        when(calculationService.calculatePayment.apply(any())).thenReturn(expectedResult);
        when(repository.findById(simulationId)).thenReturn(Mono.empty());

        Mono<CreditSimulationResult> result = useCase.calculateSimulation(simulationId, loanAmount, birthDate,
                paymentTermMonths, rateType);

        StepVerifier.create(result).expectNext(expectedResult).verifyComplete();

        verify(rateStrategyFactory).getRate(RateTypeEnum.VARIABLE);
    }
}
