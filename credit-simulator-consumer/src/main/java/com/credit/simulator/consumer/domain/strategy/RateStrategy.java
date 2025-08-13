package com.credit.simulator.consumer.domain.strategy;

import java.time.LocalDate;

public interface RateStrategy {
	Double getRate(LocalDate birthDate);
}
