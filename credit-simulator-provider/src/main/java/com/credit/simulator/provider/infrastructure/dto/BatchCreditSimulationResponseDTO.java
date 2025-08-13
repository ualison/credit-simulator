package com.credit.simulator.provider.infrastructure.dto;

import java.util.List;

public record BatchCreditSimulationResponseDTO(
    List<CreditSimulationResponseDTO> simulations,
    Integer total
) {}