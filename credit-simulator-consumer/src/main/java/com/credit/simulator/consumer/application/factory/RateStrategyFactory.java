package com.credit.simulator.consumer.application.factory;

import org.springframework.stereotype.Component;

import com.credit.simulator.consumer.domain.model.enums.RateTypeEnum;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.consumer.domain.strategy.FixedRateStrategy;
import com.credit.simulator.consumer.domain.strategy.RateStrategy;
import com.credit.simulator.consumer.domain.strategy.VariableRateStrategy;

@Component
public class RateStrategyFactory {

	public RateStrategy getRate(RateTypeEnum rateTypeEnum) {
		if (rateTypeEnum == null) throw new InvalidCreditSimulationException("rateTypeEnum can't be null");
		return switch (rateTypeEnum) {
		case FIXED -> new FixedRateStrategy();
		case VARIABLE -> new VariableRateStrategy();
		};
	}
}
