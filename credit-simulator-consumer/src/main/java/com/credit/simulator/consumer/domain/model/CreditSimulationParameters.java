package com.credit.simulator.consumer.domain.model;

import java.time.LocalDate;

public record CreditSimulationParameters(Double loanAmount, LocalDate birthDate, int paymentTermMonths, Double annualRate) {

}
