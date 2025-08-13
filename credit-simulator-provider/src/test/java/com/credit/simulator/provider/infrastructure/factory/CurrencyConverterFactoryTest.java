package com.credit.simulator.provider.infrastructure.factory;

import com.credit.simulator.provider.domain.model.exception.UnsupportedCurrencyException;
import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterFactoryTest {

    private CurrencyConverterFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CurrencyConverterFactory();
    }

    // Converte BRL para USD usando taxa correta
    @Test
    void convertsBrlToUsdCorrectly() {
        Double result = factory.convert(100.0, CurrencyTypeEnum.BRL.toString(), CurrencyTypeEnum.USD.toString());
        assertEquals(18.0, result);
    }

    // Retorna o mesmo valor para a mesma moeda
    @Test
    void returnsSameAmountForSameCurrency() {
        Double result = factory.convert(100.0, CurrencyTypeEnum.BRL.toString(), CurrencyTypeEnum.BRL.toString());
        assertEquals(100.0, result);
    }

    // Lida com conversão de valor zero
    @Test
    void handlesZeroAmountConversion() {
        Double result = factory.convert(0.0, CurrencyTypeEnum.BRL.toString(), CurrencyTypeEnum.USD.toString());
        assertEquals(0.0, result);
    }

    // Lança exceção para moeda de origem não suportada
    @Test
    void throwsExceptionForUnsupportedFromCurrency() {
        assertThrows(UnsupportedCurrencyException.class, () -> {
            factory.convert(100.0, "INVALID", CurrencyTypeEnum.USD.toString());
        });
    }

    // Obtém símbolo correto para BRL
    @Test
    void getsCorrectSymbolForBrl() {
        String symbol = factory.getSymbol(CurrencyTypeEnum.BRL.toString());
        assertEquals("R$", symbol);
    }

    // Lança exceção para símbolo de moeda não suportada
    @Test
    void throwsExceptionForUnsupportedCurrencySymbol() {
        assertThrows(UnsupportedCurrencyException.class, () -> {
            factory.getSymbol("INVALID");
        });
    }

    // Verifica se moeda não suportada retorna false
    @Test
    void returnsFalseForUnsupportedCurrency() {
        boolean isSupported = factory.isSupported("INVALID");
        assertFalse(isSupported);
    }

    // Lida com códigos de moeda insensíveis a maiúsculas
    @Test
    void handlesCaseInsensitiveCurrencyCodes() {
        Double result = factory.convert(100.0, "brl", CurrencyTypeEnum.USD.toString());
        assertEquals(18.0, result);
    }
}