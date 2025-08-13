package com.credit.simulator.consumer.domain.strategy;

import java.time.LocalDate;

import com.credit.simulator.consumer.domain.model.enums.AgeRangeEnum;
import com.credit.simulator.consumer.domain.model.shared.AgeRangeUtils;
import com.credit.simulator.consumer.domain.model.shared.Constants;

public class VariableRateStrategy implements RateStrategy {

    @Override
    public Double getRate(LocalDate birthDate) {
    	
        int age = AgeRangeUtils.calculateAge(birthDate);
        AgeRangeEnum ageRange = AgeRangeUtils.fromAge(age);
        
        return switch (ageRange) {
            case UP_TO_25 -> Constants.UP_TO_25_RATE;
            case FROM_26_TO_40 -> Constants.FROM_26_TO_40_RATE;
            case FROM_41_TO_60 -> Constants.FROM_41_TO_60_RATE;
            case ABOVE_60 -> Constants.ABOVE_60_RATE;
        };
    }
   
}