package com.credit.simulator.provider.infrastructure.messaging;

import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.provider.domain.model.enums.RateTypeEnum;
import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.provider.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProviderEventPublisherImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ProviderEventPublisherImpl eventPublisher;
    private CreditSimulation testSimulation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventPublisher = new ProviderEventPublisherImpl(rabbitTemplate);
        
        testSimulation = new CreditSimulation(
            "689ab7e9d9d99f0ed4c0aee1", 1000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, 1200.0, 100.0, 200.0, 2.0, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );
    }

    // Publica evento com sucesso
    @Test
    void publishesEventSuccessfully() {
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CreditSimulation.class));

        Mono<Void> result = eventPublisher.publishEvent(testSimulation);

        StepVerifier.create(result)
            .verifyComplete();

        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.CREDIT_EXCHANGE),
            eq(RabbitMQConfig.CREDIT_SIMULATION_ROUTING_KEY),
            eq(testSimulation)
        );
    }

    // Lida com exceção do RabbitMQ durante publicação
    @Test
    void handlesRabbitMqExceptionDuringPublishing() {
        RuntimeException rabbitException = new RuntimeException("RabbitMQ connection failed");
        doThrow(rabbitException).when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CreditSimulation.class));

        Mono<Void> result = eventPublisher.publishEvent(testSimulation);

        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();

        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.CREDIT_EXCHANGE),
            eq(RabbitMQConfig.CREDIT_SIMULATION_ROUTING_KEY),
            eq(testSimulation)
        );
    }

    // Publica evento com simulação de status completado
    @Test
    void publishesEventWithCompletedStatusSimulation() {
        CreditSimulation completedSimulation = new CreditSimulation(
            "completed-id", 1000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.COMPLETED, 1200.0, 100.0, 200.0, 2.0, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CreditSimulation.class));

        Mono<Void> result = eventPublisher.publishEvent(completedSimulation);

        StepVerifier.create(result)
            .verifyComplete();

        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.CREDIT_EXCHANGE),
            eq(RabbitMQConfig.CREDIT_SIMULATION_ROUTING_KEY),
            eq(completedSimulation)
        );
    }

    // Usa constantes corretas de configuração do RabbitMQ
    @Test
    void usesCorrectRabbitMqConfigurationConstants() {
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CreditSimulation.class));

        eventPublisher.publishEvent(testSimulation).block();

        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.CREDIT_EXCHANGE),
            eq(RabbitMQConfig.CREDIT_SIMULATION_ROUTING_KEY),
            eq(testSimulation)
        );
    }
}