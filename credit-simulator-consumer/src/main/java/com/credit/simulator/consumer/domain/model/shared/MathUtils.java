package com.credit.simulator.consumer.domain.model.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.model.CreditSimulationResult;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;


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
        simulation.setTotalInterest(roundToTwoDecimals(simulation.getTotalInterestRate()));
        simulation.setInterestRate(roundToTwoDecimals(simulation.getInterestRate()));
        
        return simulation;
    }
    
    public static CreditSimulationResult roundAllDecimals(CreditSimulationResult resultsimulation) {
    	if (resultsimulation == null) 
            throw new InvalidCreditSimulationException("Credit simulation result cannot be null");

        return new CreditSimulationResult(
            roundToTwoDecimals(resultsimulation.totalAmount()),
            roundToTwoDecimals(resultsimulation.monthlyPayment()),
            roundToTwoDecimals(resultsimulation.totalInterestRate()),
            roundToTwoDecimals(resultsimulation.interestRate())
        );
    }
}
