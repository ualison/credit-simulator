package com.credit.simulator.provider.domain.service;

import com.credit.simulator.provider.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.provider.infrastructure.dto.CreditSimulationResponseDTO;
import com.credit.simulator.provider.infrastructure.factory.CurrencyConverterFactory;

public class CurrencyService {
    
private final CurrencyConverterFactory converterFactory;
    

public CurrencyService(CurrencyConverterFactory converterFactory) {
    this.converterFactory = converterFactory;
}

public CreditSimulationResponseDTO convert(CreditSimulationResponseDTO dto, String toCurrency) {
	if(dto == null) throw new InvalidCreditSimulationException("Error: dto can't be null");
    return new CreditSimulationResponseDTO(
        dto.id(),
        converterFactory.convert(dto.loanAmount(), dto.currency(), toCurrency),
        dto.birthDate(),
        dto.paymentTermMonths(),
        dto.status(),
        converterFactory.convert(dto.totalAmount(), dto.currency(), toCurrency),
        converterFactory.convert(dto.monthlyPayment(), dto.currency(), toCurrency),
        converterFactory.convert(dto.totalInterestRate(), dto.currency(), toCurrency),
        dto.interestRate(),
        dto.interestRateType(),
        dto.currency(),
        dto.createdAt(),
        dto.updatedAt()
    );
}
}