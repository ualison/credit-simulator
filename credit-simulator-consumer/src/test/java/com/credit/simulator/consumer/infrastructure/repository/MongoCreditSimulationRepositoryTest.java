package com.credit.simulator.consumer.infrastructure.repository;

import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.consumer.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoCreditSimulationRepositoryTest {

    @Mock
    private ReactiveMongoCreditSimulationRepository springRepo;

    private MongoCreditSimulationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MongoCreditSimulationRepository(springRepo);
    }

    // Deve atualizar simulação válida e retornar resultado
    @Test
    void updateWithValidSimulation() {
        CreditSimulation simulation = createCreditSimulation();
        CreditSimulation updatedSimulation = simulation.withCalculationResult(11000.0, 916.67, 1000.0, 0.06);
        
        when(springRepo.save(any(CreditSimulation.class))).thenReturn(Mono.just(updatedSimulation));

        Mono<CreditSimulation> result = repository.update(updatedSimulation);

        StepVerifier.create(result)
            .expectNext(updatedSimulation)
            .verifyComplete();

        verify(springRepo).save(updatedSimulation);
    }

    // Deve buscar simulação por ID válido
    @Test
    void findByIdWithValidId() {
        String simulationId = "689a25e86c29ff6d44f06af4";
        CreditSimulation expectedSimulation = createCreditSimulation();
        
        when(springRepo.findById(simulationId)).thenReturn(Mono.just(expectedSimulation));

        Mono<CreditSimulation> result = repository.findById(simulationId);

        StepVerifier.create(result)
            .expectNext(expectedSimulation)
            .verifyComplete();

        verify(springRepo).findById(simulationId);
    }

    // Deve retornar vazio quando ID não existe
    @Test
    void findByIdWithNonExistentId() {
        String simulationId = "id";
        
        when(springRepo.findById(simulationId)).thenReturn(Mono.empty());

        Mono<CreditSimulation> result = repository.findById(simulationId);

        StepVerifier.create(result)
            .verifyComplete();

        verify(springRepo).findById(simulationId);
    }

    // Deve propagar erro quando falha ao buscar
    @Test
    void findByIdWhenFindFails() {
        String simulationId = "689a25e86c29ff6d44f06af4";
        RuntimeException expectedError = new RuntimeException("Database error");
        
        when(springRepo.findById(simulationId)).thenReturn(Mono.error(expectedError));

        Mono<CreditSimulation> result = repository.findById(simulationId);

        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();

        verify(springRepo).findById(simulationId);
    }


    // Deve atualizar simulação com resultado de cálculo
    @Test
    void updateWithSimulationHavingCalculationResult() {
        CreditSimulation originalSimulation = createCreditSimulation();
        CreditSimulation simulationWithResult = originalSimulation.withCalculationResult(
            12000.0, 1000.0, 2000.0, 0.08);
        
        when(springRepo.save(any(CreditSimulation.class))).thenReturn(Mono.just(simulationWithResult));

        Mono<CreditSimulation> result = repository.update(simulationWithResult);

        StepVerifier.create(result)
            .expectNext(simulationWithResult)
            .verifyComplete();

        verify(springRepo).save(simulationWithResult);
    }

    private CreditSimulation createCreditSimulation() {
        return new CreditSimulation(
            "689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, null, null, null, null, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );
    }
}