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
    
    public static CreditSimulation setDefaultValues(CreditSimulation simulation) {
        if (simulation == null) return simulation;
        
        simulation.setLoanAmount(simulation.getLoanAmount() != null ? simulation.getLoanAmount() : 0.0);
        simulation.setTotalAmount(simulation.getTotalAmount() != null ? simulation.getTotalAmount() : 0.0);
        simulation.setMonthlyPayment(simulation.getMonthlyPayment() != null ? simulation.getMonthlyPayment() : 0.0);
        simulation.setTotalInterestRate(simulation.getTotalInterestRate() != null ? simulation.getTotalInterestRate() : 0.0);
        simulation.setInterestRate(simulation.getInterestRate() != null ? simulation.getInterestRate() : 0.0);
        
        return simulation;
    }
}
