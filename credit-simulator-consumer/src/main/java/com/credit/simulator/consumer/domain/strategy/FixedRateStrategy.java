package com.credit.simulator.consumer.domain.strategy;

import java.time.LocalDate;

import com.credit.simulator.consumer.domain.model.shared.Constants;

public class FixedRateStrategy implements RateStrategy {

	@Override
	public Double getRate(LocalDate birthDate) {
		return Constants.FIXED_RATE;
	}
}