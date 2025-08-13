package com.credit.simulator.provider.domain.service;

import com.credit.simulator.provider.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.provider.infrastructure.dto.CreditSimulationResponseDTO;
import com.credit.simulator.provider.infrastructure.factory.CurrencyConverterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {
    private CurrencyConverterFactory converterFactory;
    private CurrencyService currencyService;
    private CreditSimulationResponseDTO dto;

    @BeforeEach
    void setUp() {
        converterFactory = Mockito.mock(CurrencyConverterFactory.class);
        currencyService = new CurrencyService(converterFactory);
        dto = new CreditSimulationResponseDTO(
                "id123",
                1000.0,
                LocalDate.of(1990, 1, 1),
                12,
                null,
                1200.0,
                100.0,
                200.0,
                2.0,
                "FIXED",
                "BRL",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // Deve converter todos os campos monetários usando a factory e retornar um novo DTO
    @Test
    @DisplayName("Should convert all monetary fields in DTO")
    void shouldConvertAllMonetaryFields() {
        when(converterFactory.convert(anyDouble(), anyString(), anyString())).thenReturn(10.0);
        CreditSimulationResponseDTO result = currencyService.convert(dto, "USD");
        assertEquals(10.0, result.loanAmount());
        assertEquals(10.0, result.totalAmount());
        assertEquals(10.0, result.monthlyPayment());
        assertEquals(10.0, result.totalInterestRate());
        // unchanged fields
        assertEquals(dto.id(), result.id());
        assertEquals(dto.birthDate(), result.birthDate());
        assertEquals(dto.paymentTermMonths(), result.paymentTermMonths());
        assertEquals(dto.status(), result.status());
        assertEquals(dto.interestRate(), result.interestRate());
        assertEquals(dto.interestRateType(), result.interestRateType());
        assertEquals(dto.currency(), result.currency());
        assertEquals(dto.createdAt(), result.createdAt());
        assertEquals(dto.updatedAt(), result.updatedAt());
    }

    // Deve chamar converterFactory com parâmetros corretos para cada campo
    @Test
    @DisplayName("Should call converterFactory with correct parameters")
    void shouldCallConverterFactoryWithCorrectParameters() {
        currencyService.convert(dto, "USD");
        verify(converterFactory).convert(dto.loanAmount(), dto.currency(), "USD");
        verify(converterFactory).convert(dto.totalAmount(), dto.currency(), "USD");
        verify(converterFactory).convert(dto.monthlyPayment(), dto.currency(), "USD");
        verify(converterFactory).convert(dto.totalInterestRate(), dto.currency(), "USD");
    }

    // Deve lançar InvalidCreditSimulationException se DTO for null
    @Test
    @DisplayName("Should throw NullPointerException if DTO is null")
    void shouldThrowIfDtoIsNull() {
        assertThrows(InvalidCreditSimulationException.class, () -> currencyService.convert(null, "USD"));
    }
}
