package com.credit.simulator.consumer.domain.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.credit.simulator.consumer.domain.model.shared.Constants;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FixedRateStrategyTest {

    private FixedRateStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FixedRateStrategy();
    }

    // Testa comportamento com data de nascimento nula
    @Test
    void nullBirthDate() {
        LocalDate birthDate = null;

        Double result = strategy.getRate(birthDate);

        assertEquals(Constants.FIXED_RATE, result);
    }

    // Testa se retorna taxa fixa mesmo com datas extremas
    @Test
    void extremeBirthDates() {
        LocalDate veryOld = LocalDate.of(1900, 1, 1);
        LocalDate veryYoung = LocalDate.of(2020, 12, 31);

        Double resultOld = strategy.getRate(veryOld);
        Double resultYoung = strategy.getRate(veryYoung);

        assertEquals(Constants.FIXED_RATE, resultOld);
        assertEquals(Constants.FIXED_RATE, resultYoung);
        assertEquals(resultOld, resultYoung);
    }

    // Testa consistência da taxa fixa independente da entrada
    @Test
    void consistentFixedRate() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        Double result = strategy.getRate(birthDate);

        assertEquals(Constants.FIXED_RATE, result);
    }
}