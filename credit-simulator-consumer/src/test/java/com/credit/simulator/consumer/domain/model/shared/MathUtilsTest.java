package com.credit.simulator.consumer.domain.model.shared;

import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.CreditSimulationResult;
import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    // Arredonda número com várias casas decimais
    @Test
    void roundManyDecimals() {
        Double value = 123.456789;
        Double result = MathUtils.roundToTwoDecimals(value);
        assertEquals(123.46, result, 0.001);
    }

    // Mantém número já com duas casas
    @Test
    void roundExactTwoDecimals() {
        Double value = 123.45;
        Double result = MathUtils.roundToTwoDecimals(value);
        assertEquals(123.45, result, 0.001);
    }

    // Arredonda número muito grande
    @Test
    void roundLargeValue() {
        Double value = 999999.999999;
        Double result = MathUtils.roundToTwoDecimals(value);
        assertEquals(1000000.00, result, 0.001);
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

    // Arredonda todos os campos do resultado
    @Test
    void roundResultFields() {
        CreditSimulationResult result = new CreditSimulationResult(
            11000.654321, 916.789012, 1000.456789, 0.123456
        );

        CreditSimulationResult roundedResult = MathUtils.roundAllDecimals(result);

        assertEquals(11000.65, roundedResult.totalAmount(), 0.001);
        assertEquals(916.79, roundedResult.monthlyPayment(), 0.001);
        assertEquals(1000.46, roundedResult.totalInterestRate(), 0.001);
        assertEquals(0.12, roundedResult.interestRate(), 0.001);
    }

    // Lança exceção se resultado tiver campos nulos
    @Test
    void roundResultWithNulls() {
        CreditSimulationResult result = new CreditSimulationResult(null, null, null, null);

        assertThrows(InvalidCreditSimulationException.class, () ->
            MathUtils.roundAllDecimals(result));
    }

    // Lança exceção se resultado for nulo
    @Test
    void roundNullResult() {
        assertThrows(InvalidCreditSimulationException.class, () ->
            MathUtils.roundAllDecimals((CreditSimulationResult) null));
    }

    // Arredonda valores zero
    @Test
    void roundZeroValues() {
        CreditSimulationResult result = new CreditSimulationResult(0.0, 0.0, 0.0, 0.0);

        CreditSimulationResult roundedResult = MathUtils.roundAllDecimals(result);

        assertEquals(0.00, roundedResult.totalAmount(), 0.001);
        assertEquals(0.00, roundedResult.monthlyPayment(), 0.001);
        assertEquals(0.00, roundedResult.totalInterestRate(), 0.001);
        assertEquals(0.00, roundedResult.interestRate(), 0.001);
    }
}
