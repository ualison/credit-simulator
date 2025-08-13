package com.credit.simulator.provider.infrastructure.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record BatchCreditSimulationRequestDTO(
    @NotEmpty(message = "Simulations list can't be empty")
    @Size(max = 50000, message = "50.000 simulations / request")
    @Valid List<CreditSimulationRequestDTO> simulations
) {}