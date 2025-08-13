package com.credit.simulator.provider.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreditSimulationRequestDTO(
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1000.0", message = "Minimum loan amount is R$ 1,000")
    Double loanAmount,

    @NotNull(message = "Birth date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate,

    @NotNull(message = "Payment term is required")
    @Min(value = 6, message = "Minimum payment term is 6 months")
    @Max(value = 360, message = "Maximum payment term is 360 months")
    Integer paymentTermMonths,
    
    @NotBlank(message = "Tax type is required")
    String interestRateType,
    
    @NotBlank(message = "Currency type is required")
    String currency
) {}