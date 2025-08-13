package com.credit.simulator.consumer.domain.strategy;

import com.credit.simulator.consumer.domain.model.enums.AgeRangeEnum;
import com.credit.simulator.consumer.domain.model.shared.AgeRangeUtils;
import com.credit.simulator.consumer.domain.model.shared.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VariableRateStrategyTest {

    private VariableRateStrategy variableRateStrategy;

    @BeforeEach
    void setUp() {
        variableRateStrategy = new VariableRateStrategy();
    }

    // Testa taxa para data nula
    @Test
    void getRateWithNullBirthDate() {
        try (MockedStatic<AgeRangeUtils> mockedAgeRangeUtils = Mockito.mockStatic(AgeRangeUtils.class)) {
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(null)).thenReturn(0);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(0)).thenReturn(AgeRangeEnum.UP_TO_25);

            Double result = variableRateStrategy.getRate(null);

            assertEquals(Constants.UP_TO_25_RATE, result);
        }
    }

    // Testa fronteira entre faixas etárias (25/26 anos)
    @Test
    void getRateAtAgeBoundary() {
        LocalDate birthDate25 = LocalDate.of(1999, 1, 1);
        LocalDate birthDate26 = LocalDate.of(1998, 1, 1);
        
        try (MockedStatic<AgeRangeUtils> mockedAgeRangeUtils = Mockito.mockStatic(AgeRangeUtils.class)) {
        	
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate25)).thenReturn(25);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(25)).thenReturn(AgeRangeEnum.UP_TO_25);
            
            Double result25 = variableRateStrategy.getRate(birthDate25);
            assertEquals(Constants.UP_TO_25_RATE, result25);
            
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate26)).thenReturn(26);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(26)).thenReturn(AgeRangeEnum.FROM_26_TO_40);
            
            Double result26 = variableRateStrategy.getRate(birthDate26);
            assertEquals(Constants.FROM_26_TO_40_RATE, result26);
            
            assertNotEquals(result25, result26);
        }
    }

    // Testa fronteira entre faixas etárias (60/61 anos)
    @Test
    void getRateAtAge60Boundary() {
        LocalDate birthDate60 = LocalDate.of(1964, 1, 1);
        LocalDate birthDate61 = LocalDate.of(1963, 1, 1);
        
        try (MockedStatic<AgeRangeUtils> mockedAgeRangeUtils = Mockito.mockStatic(AgeRangeUtils.class)) {
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate60)).thenReturn(60);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(60)).thenReturn(AgeRangeEnum.FROM_41_TO_60);
            
            Double result60 = variableRateStrategy.getRate(birthDate60);
            assertEquals(Constants.FROM_41_TO_60_RATE, result60);
            
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate61)).thenReturn(61);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(61)).thenReturn(AgeRangeEnum.ABOVE_60);
            
            Double result61 = variableRateStrategy.getRate(birthDate61);
            assertEquals(Constants.ABOVE_60_RATE, result61);
            
            assertNotEquals(result60, result61);
        }
    }

    // Testa idade extremamente alta
    @Test
    void getRateWithExtremeAge() {
        LocalDate veryOldBirthDate = LocalDate.of(1900, 1, 1);
        
        try (MockedStatic<AgeRangeUtils> mockedAgeRangeUtils = Mockito.mockStatic(AgeRangeUtils.class)) {
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(veryOldBirthDate)).thenReturn(124);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(124)).thenReturn(AgeRangeEnum.ABOVE_60);

            Double result = variableRateStrategy.getRate(veryOldBirthDate);

            assertEquals(Constants.ABOVE_60_RATE, result);
        }
    }

    // Testa todas as faixas etárias principais
    @Test
    void getRateForAllAgeRanges() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        
        try (MockedStatic<AgeRangeUtils> mockedAgeRangeUtils = Mockito.mockStatic(AgeRangeUtils.class)) {
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate)).thenReturn(20);
            
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(20)).thenReturn(AgeRangeEnum.UP_TO_25);
            assertEquals(Constants.UP_TO_25_RATE, variableRateStrategy.getRate(birthDate));
            
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate)).thenReturn(35);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(35)).thenReturn(AgeRangeEnum.FROM_26_TO_40);
            assertEquals(Constants.FROM_26_TO_40_RATE, variableRateStrategy.getRate(birthDate));
            
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate)).thenReturn(50);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(50)).thenReturn(AgeRangeEnum.FROM_41_TO_60);
            assertEquals(Constants.FROM_41_TO_60_RATE, variableRateStrategy.getRate(birthDate));
            
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.calculateAge(birthDate)).thenReturn(70);
            mockedAgeRangeUtils.when(() -> AgeRangeUtils.fromAge(70)).thenReturn(AgeRangeEnum.ABOVE_60);
            assertEquals(Constants.ABOVE_60_RATE, variableRateStrategy.getRate(birthDate));
        }
    }
}