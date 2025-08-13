package com.credit.simulator.consumer.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.credit.simulator.consumer.domain.model.enums.AgeRangeEnum;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.consumer.domain.model.shared.AgeRangeUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgeRangeUtilsTest {

    // Deve retornar verdadeiro quando idade estiver na faixa válida
    @Test
    void containsWithValidAgeInRange() {
        AgeRangeEnum range = AgeRangeEnum.FROM_26_TO_40;
        int age = 30;

        boolean result = AgeRangeUtils.contains(range, age);

        assertTrue(result);
    }

    // Deve retornar a faixa etária correta para idade válida
    @Test
    void fromAgeWithValidAge() {
        int age = 30;

        AgeRangeEnum result = AgeRangeUtils.fromAge(age);

        assertEquals(AgeRangeEnum.FROM_26_TO_40, result);
    }

    // Deve retornar faixa até 25 para idade de 25 anos
    @Test
    void fromAgeWithAge25() {
        int age = 25;

        AgeRangeEnum result = AgeRangeUtils.fromAge(age);

        assertEquals(AgeRangeEnum.UP_TO_25, result);
    }

    // Deve retornar faixa de 41 a 60 para idade de 60 anos
    @Test
    void fromAgeWithAge60() {
        int age = 60;

        AgeRangeEnum result = AgeRangeUtils.fromAge(age);

        assertEquals(AgeRangeEnum.FROM_41_TO_60, result);
    }

    // Deve retornar faixa acima de 60 para idade superior a 60 anos
    @Test
    void fromAgeWithAgeAbove60() {
        int age = 65;

        AgeRangeEnum result = AgeRangeUtils.fromAge(age);

        assertEquals(AgeRangeEnum.ABOVE_60, result);
    }

    // Deve lançar exceção para idade inválida
    @Test
    void fromAgeWithInvalidAge() {
        int age = -1;

        assertThrows(InvalidCreditSimulationException.class, () -> AgeRangeUtils.fromAge(age));
    }

    // Deve retornar faixa etária correta a partir da data de nascimento
    @Test
    void fromBirthDateWithValidBirthDate() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

            AgeRangeEnum result = AgeRangeUtils.fromBirthDate(birthDate);

            assertEquals(AgeRangeEnum.FROM_26_TO_40, result);
    }

    // Deve lançar exceção quando data de nascimento for nula
    @Test
    void fromBirthDateWithNullBirthDate() {
        LocalDate birthDate = null;

        assertThrows(InvalidCreditSimulationException.class, () -> AgeRangeUtils.fromBirthDate(birthDate));
    }

    // Deve calcular idade correta a partir da data de nascimento
    @Test
    void calculateAgeWithValidBirthDate() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

            int result = AgeRangeUtils.calculateAge(birthDate);

            assertEquals(35, result);
    }

    // Deve retornar faixa até 25 para idade zero
    @Test
    void fromAgeWithAge0() {
        int age = 0;

        AgeRangeEnum result = AgeRangeUtils.fromAge(age);

        assertEquals(AgeRangeEnum.UP_TO_25, result);
    }

    // Deve retornar faixa acima de 60 para idade de 100 anos
    @Test
    void fromAgeWithAge100() {
        int age = 100;

        AgeRangeEnum result = AgeRangeUtils.fromAge(age);

        assertEquals(AgeRangeEnum.ABOVE_60, result);
    }

    // Deve retornar resultados corretos para todas as faixas etárias
    @Test
    void containsWithAllAgeRanges() {
        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.UP_TO_25, 0));
        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.UP_TO_25, 25));
        assertFalse(AgeRangeUtils.contains(AgeRangeEnum.UP_TO_25, 26));

        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.FROM_26_TO_40, 26));
        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.FROM_26_TO_40, 40));
        assertFalse(AgeRangeUtils.contains(AgeRangeEnum.FROM_26_TO_40, 25));
        assertFalse(AgeRangeUtils.contains(AgeRangeEnum.FROM_26_TO_40, 41));

        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.FROM_41_TO_60, 41));
        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.FROM_41_TO_60, 60));
        assertFalse(AgeRangeUtils.contains(AgeRangeEnum.FROM_41_TO_60, 40));
        assertFalse(AgeRangeUtils.contains(AgeRangeEnum.FROM_41_TO_60, 61));

        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.ABOVE_60, 61));
        assertTrue(AgeRangeUtils.contains(AgeRangeEnum.ABOVE_60, 100));
        assertFalse(AgeRangeUtils.contains(AgeRangeEnum.ABOVE_60, 60));
    }
}