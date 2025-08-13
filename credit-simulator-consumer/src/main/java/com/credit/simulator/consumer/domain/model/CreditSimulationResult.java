package com.credit.simulator.consumer.domain.model;


public record CreditSimulationResult(
	    Double totalAmount,
	    Double monthlyPayment,
	    Double totalInterestRate,
	    Double interestRate
	) {
}