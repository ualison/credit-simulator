package com.credit.simulator.provider.domain.model.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.credit.simulator.provider.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.provider.domain.model.CreditSimulation;

public class MathUtils {
	
	public static Double roundToTwoDecimals(Double value) {
        if (value == null) throw new InvalidCreditSimulationException("roundToTwoDecimals: valeu can't be null");
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    public static CreditSimulation roundAllDecimals(CreditSimulation simulation) {
    	if (simulation == null) 
            throw new InvalidCreditSimulationException("Credit simulation cannot be null");

        simulation.setLoanAmount(roundToTwoDecimals(simulation.getLoanAmount()));
        simulation.setTotalAmount(roundToTwoDecimals(simulation.getTotalAmount()));
        simulation.setMonthlyPayment(roundToTwoDecimals(simulation.getMonthlyPayment()));
        simulation.setTotalInterestRate(roundToTwoDecimals(simulation.getTotalInterestRate()));
        simulation.setInterestRate(roundToTwoDecimals(simulation.getInterestRate()));
        
        return simulation;
    }
}
