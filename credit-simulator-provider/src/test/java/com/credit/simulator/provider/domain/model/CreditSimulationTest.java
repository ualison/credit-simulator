package com.credit.simulator.provider.domain.model;

import org.junit.jupiter.api.Test;

import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreditSimulationTest {

    private final String testId = "test-id-123";
    private final Double testLoanAmount = 10000.0;
    private final LocalDate testBirthDate = LocalDate.of(1990, 5, 15);
    private final Integer testPaymentTermMonths = 24;
    private final CreditSimulationStatusEnum testStatus = CreditSimulationStatusEnum.PENDING;
    private final Double testTotalAmount = 12000.0;
    private final Double testMonthlyPayment = 500.0;
    private final Double testTotalInterestRate = 2000.0;
    private final Double testInterestRate = 2.5;
    private final String testInterestRateType = "FIXED";
    private final String testCurrency = "BRL";
    private final LocalDateTime testCreatedAt = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
    private final LocalDateTime testUpdatedAt = LocalDateTime.of(2023, 1, 2, 15, 30, 0);

    // Teste: Cria instância com construtor padrão
    @Test
    void createInstanceWithDefaultConstructor() {
        CreditSimulation simulation = new CreditSimulation();
        
        assertNotNull(simulation);
        assertNull(simulation.getId());
        assertNull(simulation.getLoanAmount());
        assertNull(simulation.getBirthDate());
        assertNull(simulation.getPaymentTermMonths());
        assertNull(simulation.getStatus());
        assertNull(simulation.getTotalAmount());
        assertNull(simulation.getMonthlyPayment());
        assertNull(simulation.getTotalInterestRate());
        assertNull(simulation.getInterestRate());
        assertNull(simulation.getInterestRateType());
        assertNull(simulation.getCurrency());
        assertNull(simulation.getCreatedAt());
        assertNull(simulation.getUpdatedAt());
    }

    // Teste: Cria instância com construtor completo
    @Test
    void createInstanceWithFullConstructor() {
        CreditSimulation simulation = new CreditSimulation(
            testId, testLoanAmount, testBirthDate, testPaymentTermMonths,
            testStatus, testTotalAmount, testMonthlyPayment, testTotalInterestRate,
            testInterestRate, testInterestRateType, testCurrency, testCreatedAt, testUpdatedAt
        );
        
        assertNotNull(simulation);
        assertEquals(testId, simulation.getId());
        assertEquals(testLoanAmount, simulation.getLoanAmount());
        assertEquals(testBirthDate, simulation.getBirthDate());
        assertEquals(testPaymentTermMonths, simulation.getPaymentTermMonths());
        assertEquals(testStatus, simulation.getStatus());
        assertEquals(testTotalAmount, simulation.getTotalAmount());
        assertEquals(testMonthlyPayment, simulation.getMonthlyPayment());
        assertEquals(testTotalInterestRate, simulation.getTotalInterestRate());
        assertEquals(testInterestRate, simulation.getInterestRate());
        assertEquals(testInterestRateType, simulation.getInterestRateType());
        assertEquals(testCurrency, simulation.getCurrency());
        assertEquals(testCreatedAt, simulation.getCreatedAt());
        assertEquals(testUpdatedAt, simulation.getUpdatedAt());
    }

    // Teste: Cria simulação pendente com factory method
    @Test
    void createPendingSimulation() {
        CreditSimulation simulation = CreditSimulation.createPending(
            testLoanAmount, testBirthDate, testPaymentTermMonths, 
            testInterestRateType, testCurrency
        );
        
        assertNotNull(simulation);
        assertNull(simulation.getId());
        assertEquals(testLoanAmount, simulation.getLoanAmount());
        assertEquals(testBirthDate, simulation.getBirthDate());
        assertEquals(testPaymentTermMonths, simulation.getPaymentTermMonths());
        assertEquals(CreditSimulationStatusEnum.PENDING, simulation.getStatus());
        assertNull(simulation.getTotalAmount());
        assertNull(simulation.getMonthlyPayment());
        assertNull(simulation.getTotalInterestRate());
        assertNull(simulation.getInterestRate());
        assertEquals(testInterestRateType, simulation.getInterestRateType());
        assertEquals(testCurrency, simulation.getCurrency());
        assertNotNull(simulation.getCreatedAt());
        assertNotNull(simulation.getUpdatedAt());
    }


}