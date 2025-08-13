package com.credit.simulator.consumer.domain.model.exception;

public class InvalidCreditSimulationException extends RuntimeException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCreditSimulationException(String message) {
        super(message);
    }

}