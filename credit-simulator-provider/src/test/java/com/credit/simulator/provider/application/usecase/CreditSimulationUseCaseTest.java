package com.credit.simulator.provider.application.usecase;

import com.credit.simulator.provider.application.messagin.ProviderEventPublisher;
import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.provider.domain.model.enums.RateTypeEnum;
import com.credit.simulator.provider.domain.repository.CreditSimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CreditSimulationUseCaseTest {

    @Mock
    private CreditSimulationRepository repository;

    @Mock
    private ProviderEventPublisher eventPublisher;

    private CreditSimulationUseCase useCase;

    private CreditSimulation testSimulation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreditSimulationUseCase(repository, eventPublisher);
        
        testSimulation = new CreditSimulation(
            "689ab7e9d9d99f0ed4c0aee1", 1000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, 1200.0, 100.0, 200.0, 2.0, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );
    }

    // Encontra simulação por ID com sucesso
    @Test
    void findSimulationById() {
        when(repository.findById("689ab7e9d9d99f0ed4c0aee1")).thenReturn(Mono.just(testSimulation));

        StepVerifier.create(useCase.execute("689ab7e9d9d99f0ed4c0aee1"))
            .expectNext(testSimulation)
            .verifyComplete();

        verify(repository).findById("689ab7e9d9d99f0ed4c0aee1");
    }

    //Retorna Mono vazio quando simulação não é encontrada
    @Test
    void returnEmptyWhenNotFound() {
        when(repository.findById("non-existent")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("non-existent"))
            .verifyComplete();

        verify(repository).findById("non-existent");
    }

    //Processa lote de simulações com sucesso
    @Test
    void processBatchOfSimulations() {
        List<CreditSimulation> simulations = Arrays.asList(testSimulation);
        when(repository.saveAll(anyList())).thenReturn(Flux.fromIterable(simulations));
        when(eventPublisher.publishEvent(any(CreditSimulation.class))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(simulations))
            .expectNext(Arrays.asList(testSimulation))
            .verifyComplete();

        verify(repository).saveAll(anyList());
        verify(eventPublisher).publishEvent(testSimulation);
    }

    // Processa lote grande em blocos de 1000
    @Test
    void processLargeBatchInChunks() {
        List<CreditSimulation> simulations = Arrays.asList(testSimulation, testSimulation, testSimulation);
        when(repository.saveAll(anyList())).thenReturn(Flux.fromIterable(simulations));
        when(eventPublisher.publishEvent(any(CreditSimulation.class))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(simulations))
            .expectNext(Arrays.asList(testSimulation, testSimulation, testSimulation))
            .verifyComplete();

        verify(repository, times(1)).saveAll(anyList());
        verify(eventPublisher, times(3)).publishEvent(any(CreditSimulation.class));
    }

    // Lida com lote vazio de simulações
    @Test
    void handleEmptyBatch() {
        List<CreditSimulation> simulations = Arrays.asList();

        StepVerifier.create(useCase.execute(simulations))
            .expectNext(Arrays.asList())
            .verifyComplete();

        verify(repository, never()).saveAll(anyList());
        verify(eventPublisher, never()).publishEvent(any(CreditSimulation.class));
    }



    //Encontra todas as simulações com sucesso
    @Test
    void findAllSimulations() {
        List<CreditSimulation> simulations = Arrays.asList(testSimulation);
        when(repository.findAll()).thenReturn(Flux.fromIterable(simulations));

        StepVerifier.create(useCase.findAllSimulations())
            .expectNext(testSimulation)
            .verifyComplete();

        verify(repository).findAll();
    }

    // Retorna Flux vazio quando não existem simulações
    @Test
    void returnEmptyWhenNoSimulations() {
        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.findAllSimulations())
            .verifyComplete();

        verify(repository).findAll();
    }


    // Conta simulações com sucesso
    @Test
    void countSimulations() {
        when(repository.count()).thenReturn(Mono.just(5L));

        StepVerifier.create(useCase.countSimulations())
            .expectNext(5L)
            .verifyComplete();

        verify(repository).count();
    }

    // Retorna contagem zero quando não existem simulações
    @Test
    void returnZeroCountWhenNoSimulations() {
        when(repository.count()).thenReturn(Mono.just(0L));

        StepVerifier.create(useCase.countSimulations())
            .expectNext(0L)
            .verifyComplete();

        verify(repository).count();
    }
}