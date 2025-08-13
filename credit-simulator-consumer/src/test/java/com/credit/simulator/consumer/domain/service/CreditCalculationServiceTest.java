package com.credit.simulator.consumer.domain.service;

import com.credit.simulator.consumer.domain.model.CreditSimulationResult;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.consumer.domain.model.CreditSimulationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditCalculationServiceTest {

    private CreditCalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new CreditCalculationService();
    }

    // Calcula empréstimo padrão com parâmetros típicos
    @Test
    void standardLoan() {
        CreditSimulationParameters params = new CreditSimulationParameters(10000.0, LocalDate.of(1990, 1, 1), 12, 0.06);

        CreditSimulationResult result = calculationService.calculatePayment.apply(params);

        assertEquals(0.06, result.interestRate());
        assertTrue(result.totalAmount() > 10000.0);
        assertTrue(result.monthlyPayment() > 0);
        assertTrue(result.totalInterestRate() > 0);
        assertEquals(result.totalAmount(), result.monthlyPayment() * 12, 0.01);
    }

    // Calcula com taxa de juros zero (divisão simples)
    @Test
    void zeroInterestRate() {
        CreditSimulationParameters params = new CreditSimulationParameters(12000.0, LocalDate.of(1990, 1, 1), 12, 0.0);

        CreditSimulationResult result = calculationService.calculatePayment.apply(params);

        assertEquals(0.0, result.interestRate());
        assertEquals(12000.0, result.totalAmount(), 0.01);
        assertEquals(1000.0, result.monthlyPayment(), 0.01);
        assertEquals(0.0, result.totalInterestRate(), 0.01);
    }

    // Verifica cálculo da fórmula PMT
    @Test
    void monthlyPaymentFormula() {
        CreditSimulationParameters params = new CreditSimulationParameters(10000.0, LocalDate.of(1990, 1, 1), 12, 0.06);
        double monthlyRate = 0.06 / 12;
        double expectedPMT = (10000.0 * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -12));

        Double monthlyPayment = CreditCalculationService.calculateMonthlyPayment.apply(params);

        assertEquals(expectedPMT, monthlyPayment, 0.01);
    }

    // Calcula empréstimo de um mês
    @Test
    void oneMonthLoan() {
        CreditSimulationParameters params = new CreditSimulationParameters(10000.0, LocalDate.of(1990, 1, 1), 1, 0.06);

        CreditSimulationResult result = calculationService.calculatePayment.apply(params);

        assertEquals(result.totalAmount(), result.monthlyPayment(), 0.01);
        assertTrue(result.totalAmount() > 10000.0);
    }

    // Lança exceção para parâmetros nulos
    @Test
    void nullParameters() {
        assertThrows(InvalidCreditSimulationException.class, () ->
            calculationService.calculatePayment.apply(null));
    }

    // Lança exceção para valor negativo
    @Test
    void negativeLoanAmount() {
        CreditSimulationParameters params = new CreditSimulationParameters(-1000.0, LocalDate.of(1990, 1, 1), 12, 0.06);

        assertThrows(InvalidCreditSimulationException.class, () ->
            calculationService.calculatePayment.apply(params));
    }

    // Lança exceção para prazo zero
    @Test
    void zeroPaymentTerm() {
        CreditSimulationParameters params = new CreditSimulationParameters(10000.0, LocalDate.of(1990, 1, 1), 0, 0.06);

        assertThrows(InvalidCreditSimulationException.class, () ->
            calculationService.calculatePayment.apply(params));
    }

    // Lança exceção para taxa negativa
    @Test
    void negativeInterestRate() {
        CreditSimulationParameters params = new CreditSimulationParameters(10000.0, LocalDate.of(1990, 1, 1), 12, -0.05);

        assertThrows(InvalidCreditSimulationException.class, () ->
            calculationService.calculatePayment.apply(params));
    }
}
