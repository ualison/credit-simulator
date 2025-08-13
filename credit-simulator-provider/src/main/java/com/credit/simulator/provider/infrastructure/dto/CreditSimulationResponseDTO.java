package com.credit.simulator.provider.infrastructure.dto;

import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreditSimulationResponseDTO(
    String id,
    Double loanAmount,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
    Integer paymentTermMonths,
    CreditSimulationStatusEnum status,
    Double totalAmount,
    Double monthlyPayment,
    Double totalInterestRate,
    Double interestRate,
    String interestRateType,
    String currency,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAt
) {}