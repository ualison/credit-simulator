package com.credit.simulator.provider.domain.model.shared;

import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.provider.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.enums.RateTypeEnum;
import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    // Arredonda número decimal positivo para duas casas decimais
    @Test
    void roundPositiveDecimal() {
        Double result = MathUtils.roundToTwoDecimals(123.456121);
        assertEquals(123.46, result);
    }

    // Arredonda número decimal negativo para duas casas decimais
    @Test
    void roundNegativeDecimal() {
        Double result = MathUtils.roundToTwoDecimals(-123.456);
        assertEquals(-123.46, result);
    }

    // Arredonda número muito pequeno
    @Test
    void roundVerySmallDecimal() {
        Double result = MathUtils.roundToTwoDecimals(0.001);
        assertEquals(0.00, result);
    }

    //  Arredonda todos os campos decimais em CreditSimulation
    @Test
    void roundAllDecimalsInCreditSimulation() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        LocalDateTime now = LocalDateTime.now();
        
        CreditSimulation simulation = new CreditSimulation(
            "689ab7e9d9d99f0ed4c0aee1", 1000.123, birthDate, 12, CreditSimulationStatusEnum.PENDING,
            1200.456, 100.789, 200.321, 2.654, RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), now, now
        );
        
        CreditSimulation rounded = MathUtils.roundAllDecimals(simulation);
        
        assertEquals(1000.12, rounded.getLoanAmount());
        assertEquals(1200.46, rounded.getTotalAmount());
        assertEquals(100.79, rounded.getMonthlyPayment());
        assertEquals(200.32, rounded.getTotalInterestRate());
        assertEquals(2.65, rounded.getInterestRate());
    }
    
    // Lança exceção se simulação tiver campos nulos
    @Test
    void roundSimulationWithNulls() {
        CreditSimulation simulation = new CreditSimulation(
            "test-id", null, LocalDate.of(1990, 1, 1), 12,
            CreditSimulationStatusEnum.PENDING, null, null, null, null,
            "FIXED", "BRL", LocalDateTime.now(), LocalDateTime.now()
        );

        assertThrows(InvalidCreditSimulationException.class, () ->
            MathUtils.roundAllDecimals(simulation));
    }

    // Lança exceção se simulação for nula
    @Test
    void roundNullSimulation() {
        assertThrows(InvalidCreditSimulationException.class, () ->
            MathUtils.roundAllDecimals((CreditSimulation) null));
    }
}