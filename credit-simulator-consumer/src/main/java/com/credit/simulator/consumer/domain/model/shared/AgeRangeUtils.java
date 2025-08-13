package com.credit.simulator.consumer.domain.model.shared;

import java.util.Arrays;

import java.time.LocalDate;
import java.time.Period;

import com.credit.simulator.consumer.domain.model.enums.AgeRangeEnum;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;

public final class AgeRangeUtils {

	public static boolean contains(AgeRangeEnum range, int age) {
		return age >= range.getMinAge() && age <= range.getMaxAge();
	}

	public static AgeRangeEnum fromAge(int age) {

		return Arrays.stream(AgeRangeEnum.values()).filter(range -> contains(range, age)).findFirst()
				.orElseThrow(() -> new InvalidCreditSimulationException("Invalid age: " + age));
	}

	public static AgeRangeEnum fromBirthDate(LocalDate birthDate) {
		if (birthDate == null)
			throw new InvalidCreditSimulationException("Birth date cannot be null");

		int age = calculateAge(birthDate);
		return fromAge(age);
	}

	public static int calculateAge(LocalDate birthDate) {
		if (birthDate == null)
			throw new InvalidCreditSimulationException("Birth date cannot be null");
		return Period.between(birthDate, LocalDate.now()).getYears();
	}
}