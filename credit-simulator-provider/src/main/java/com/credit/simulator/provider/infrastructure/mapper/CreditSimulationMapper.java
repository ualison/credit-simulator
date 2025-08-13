package com.credit.simulator.provider.infrastructure.mapper;

import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.infrastructure.dto.*;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CreditSimulationMapper {

    public CreditSimulation toDomain(CreditSimulationRequestDTO dto) {  
        return CreditSimulation.createPending(dto.loanAmount(), dto.birthDate(), dto.paymentTermMonths(), dto.interestRateType(), dto.currency());
    }

    public CreditSimulationResponseDTO toResponseDTO(CreditSimulation domain) {
        return new CreditSimulationResponseDTO(
            domain.getId(), 
            domain.getLoanAmount(), 
            domain.getBirthDate(),
            domain.getPaymentTermMonths(), 
            domain.getStatus(), 
            domain.getTotalAmount(), 
            domain.getMonthlyPayment(),
            domain.getTotalInterestRate(), 
            domain.getInterestRate(), 
            domain.getInterestRateType(),
            domain.getCurrency(),
            domain.getCreatedAt(), 
            domain.getUpdatedAt()
        );
    }

    public BatchCreditSimulationResponseDTO toLargeResponseDTO(List<CreditSimulation> simulations) {
        return new BatchCreditSimulationResponseDTO(
            simulations.stream().map(this::toResponseDTO).toList(),
            simulations.size()
        );
    }
}