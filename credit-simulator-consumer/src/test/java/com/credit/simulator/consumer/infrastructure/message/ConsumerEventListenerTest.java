package com.credit.simulator.consumer.infrastructure.message;

import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.consumer.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.infrastructure.worker.CreditSimulationCalculateWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumerEventListenerTest {

    @Mock
    private CreditSimulationCalculateWorker worker;

    @Mock
    private ObjectMapper objectMapper;

    private ConsumerEventListener eventListener;

    @BeforeEach
    void setUp() {
        eventListener = new ConsumerEventListener(worker, objectMapper);
    }

    // Deve processar simulação de crédito válida com sucesso
    @Test
    void consumeWithValidCreditSimulation() {
        CreditSimulation creditSimulation = createCreditSimulation();

        eventListener.consume(creditSimulation);

        verify(worker).processSimulation(creditSimulation);
        verifyNoMoreInteractions(worker);
    }

    // Deve processar simulação com status completo
    @Test
    void consumeWithCompletedStatusSimulation() {
        CreditSimulation creditSimulation = new CreditSimulation(
            "689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.COMPLETED, 11000.0, 916.67, 1000.0, 0.06, 
             RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );

        eventListener.consume(creditSimulation);

        verify(worker).processSimulation(creditSimulation);
    }

    // Deve processar simulação com status pendente
    @Test
    void consumeWithPendingStatusSimulation() {
        CreditSimulation creditSimulation = new CreditSimulation(
            "689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, null, null, null, null, 
             RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );

        eventListener.consume(creditSimulation);

        verify(worker).processSimulation(creditSimulation);
    }

    // Deve processar simulação com taxa variável
    @Test
    void consumeWithVariableRateType() {
        CreditSimulation creditSimulation = new CreditSimulation(
            "689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, null, null, null, null, 
            "VARIABLE", CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );

        eventListener.consume(creditSimulation);

        verify(worker).processSimulation(creditSimulation);
    }

    // Deve processar simulação com moeda diferente
    @Test
    void consumeWithDifferentCurrency() {
        CreditSimulation creditSimulation = new CreditSimulation(
            "689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, null, null, null, null, 
             RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.USD.toString(), LocalDateTime.now(), LocalDateTime.now()
        );

        eventListener.consume(creditSimulation);

        verify(worker).processSimulation(creditSimulation);
    }
    

    private CreditSimulation createCreditSimulation() {
        return new CreditSimulation(
            "689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, null, null, null, null, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );
    }
}