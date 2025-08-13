package com.credit.simulator.provider.domain.model.exception;

public class UnsupportedCurrencyException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedCurrencyException(String message) {
        super(message);
    }
}