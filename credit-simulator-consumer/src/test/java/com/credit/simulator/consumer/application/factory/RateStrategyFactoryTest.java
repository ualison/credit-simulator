package com.credit.simulator.consumer.application.factory;

import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.consumer.domain.strategy.FixedRateStrategy;
import com.credit.simulator.consumer.domain.strategy.RateStrategy;
import com.credit.simulator.consumer.domain.strategy.VariableRateStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateStrategyFactoryTest {

    private RateStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new RateStrategyFactory();
    }

    // Verifica se ao solicitar uma taxa FIXA, a fábrica nos dá a estratégia correta
    @Test
    void returnFixedStrategyForFixedRate() {
        RateTypeEnum rateType = RateTypeEnum.FIXED;

        RateStrategy result = factory.getRate(rateType);

        assertNotNull(result);
        assertInstanceOf(FixedRateStrategy.class, result);
    }

    // Verifica se ao pedir uma taxa VARIÁVEL, a fábrica nos dá a estratégia certa
    @Test
    void returnVariableStrategyForVariableRate() {
        RateTypeEnum rateType = RateTypeEnum.VARIABLE;

        RateStrategy result = factory.getRate(rateType);

        assertNotNull(result);
        assertInstanceOf(VariableRateStrategy.class, result);
    }

    // Teste para garantir que um valor nulo para o tipo de taxa cause um erro esperado
    @Test
    void throwExceptionForNullRateType() {
        RateTypeEnum rateType = null;

        assertThrows(InvalidCreditSimulationException.class, () -> factory.getRate(rateType));
    }
}
