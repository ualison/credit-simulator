package com.credit.simulator.consumer.domain.model;

import org.junit.jupiter.api.Test;

import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.consumer.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.domain.model.shared.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreditSimulationTest {

    private CreditSimulation createBasicSimulation() {
        return new CreditSimulation("689a25e86c29ff6d44f06af4", 10000.0, LocalDate.of(1990, 1, 1), Constants.YEAR_MONTHS,
                CreditSimulationStatusEnum.PENDING, null, null, null, null, RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(),
                LocalDateTime.now(), LocalDateTime.now());
    }

    // Testa se withCalculationResult cria nova instância imutável com dados válidos
    @Test
    void calculationResultNewInstance() {
    	
        CreditSimulation original = createBasicSimulation();
        CreditSimulation result = original.withCalculationResult(11000.0, 916.67, 1000.0, 0.06);

        assertNotSame(original, result);
        assertEquals(CreditSimulationStatusEnum.COMPLETED, result.getStatus());
        assertEquals(11000.0, result.getTotalAmount());
        assertEquals(916.67, result.getMonthlyPayment());
        assertEquals(1000.0, result.getTotalInterestRate());
        assertEquals(0.06, result.getInterestRate());
        
    }

    // Testa se o objeto original permanece inalterado após withCalculationResult
    @Test
    void calculationResultUnchanged() {
        CreditSimulation original = createBasicSimulation();
        original.withCalculationResult(11000.0, 916.67, 1000.0, 0.06);

        assertEquals(CreditSimulationStatusEnum.PENDING, original.getStatus());
        assertNull(original.getTotalAmount());
        assertNull(original.getMonthlyPayment());
    }

}
