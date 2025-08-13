package com.credit.simulator.consumer.application.usecase;

import com.credit.simulator.consumer.application.factory.RateStrategyFactory;
import com.credit.simulator.consumer.domain.model.CreditSimulationResult;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.domain.model.CreditSimulationParameters;
import com.credit.simulator.consumer.domain.model.shared.MathUtils;
import com.credit.simulator.consumer.domain.strategy.RateStrategy;
import com.credit.simulator.consumer.domain.repository.CreditSimulationRepository;
import com.credit.simulator.consumer.domain.service.CreditCalculationService;
import com.credit.simulator.consumer.domain.model.CreditSimulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Service
public class CreditSimulationUseCase {

    private final CreditSimulationRepository repository;
    private final CreditCalculationService calculationService;
    private final RateStrategyFactory rateStrategyFactory;
    
    private static final Logger logger = LoggerFactory.getLogger(CreditSimulationUseCase.class);

    public CreditSimulationUseCase(CreditSimulationRepository repository, CreditCalculationService calculationService,
            RateStrategyFactory rateStrategyFactory) {
        this.repository = repository;
        this.calculationService = calculationService;
        this.rateStrategyFactory = rateStrategyFactory;
    }
    
    public Mono<CreditSimulationResult> calculateSimulation(String simulationId, Double loanAmount, LocalDate birthDate,
            Integer paymentTermMonths, RateTypeEnum rateTypeEnum) {

        return Mono.fromSupplier(() -> {
            logger.debug("Processing simulation: {}", simulationId);

            RateStrategy rateTypeStrategy = rateStrategyFactory.getRate(rateTypeEnum);
            Double annualRate = rateTypeStrategy.getRate(birthDate);
            CreditSimulationParameters loanParameters = new CreditSimulationParameters(loanAmount, birthDate, paymentTermMonths, annualRate);

            return calculationService.calculatePayment.apply(loanParameters);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext(result -> {

            updateDataSimulation(simulationId, result)
                    .subscribeOn(Schedulers.parallel())
                    .subscribe(
                            success -> logger.debug("Updated simulation: {}", simulationId),
                            error -> logger.warn("Failed to update simulation {}: {}", simulationId, error.getMessage())
                    );
        })
        .doOnSuccess(result -> logger.debug("Completed simulation: {}", simulationId))
        .doOnError(error -> logger.error("Failed simulation {}: {}", simulationId, error.getMessage()));
    }

    private Mono<CreditSimulation> updateDataSimulation(String simulationId, CreditSimulationResult creditSimulationResult) {
        return repository.findById(simulationId)
                .cast(CreditSimulation.class)
                .map(simulation -> simulation.withCalculationResult(
                		creditSimulationResult.totalAmount(), 
                		creditSimulationResult.monthlyPayment(), 
                		creditSimulationResult.totalInterestRate(), 
                		creditSimulationResult.interestRate()))
                .map(MathUtils::roundAllDecimals)
                .flatMap(repository::update);
    }
}