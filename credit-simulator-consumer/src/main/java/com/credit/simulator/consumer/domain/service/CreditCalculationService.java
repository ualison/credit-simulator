package com.credit.simulator.consumer.domain.service;

import com.credit.simulator.consumer.domain.model.CreditSimulationResult;
import com.credit.simulator.consumer.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.consumer.domain.model.CreditSimulationParameters;
import com.credit.simulator.consumer.domain.model.shared.Constants;

import java.util.function.Function;


public class CreditCalculationService {

    /**
     * Método para calcular o simulação de crédito
     */
    public Function<CreditSimulationParameters, CreditSimulationResult> calculatePayment = (params) -> {
        validateParameters(params);
        
        double monthlyPayment = calculateMonthlyPayment.apply(params);
        double totalAmount = monthlyPayment * params.paymentTermMonths();
        double totalInterest = totalAmount - params.loanAmount();

        return new CreditSimulationResult(totalAmount, monthlyPayment, totalInterest, params.annualRate());
    };

    /**
     * PMT = Pagamento mensal
     * PMT = (PV × r) / (1 - (1 + r)^(-n))
     * PV = Valor presente (loan amount)
     * r = Taxa de juros mensal (annual rate / 12)
     * n = Número total de pagamentos (paymentTermMonths)
     */
    public static Function<CreditSimulationParameters, Double> calculateMonthlyPayment = params -> {
    	
        validateParameters(params);
        
        if (params.annualRate() == 0.0)
            return params.loanAmount() / params.paymentTermMonths();

        double monthlyRate = params.annualRate() / Constants.YEAR_MONTHS;
        double numerator = params.loanAmount() * monthlyRate;
        double factor = Math.pow(1 + monthlyRate, -params.paymentTermMonths());
        double denominator = 1 - factor;

        return numerator / denominator;
    };


    private static void validateParameters(CreditSimulationParameters params) {
        if (params == null) 
            throw new InvalidCreditSimulationException("Loan parameters cannot be null");
        
        if (params.loanAmount() == null || params.loanAmount() <= 0) 
            throw new InvalidCreditSimulationException("Loan amount must be positive");
        
        if (params.paymentTermMonths() <= 0) 
            throw new InvalidCreditSimulationException("Payment term must be positive");
        
        if (params.annualRate() == null || params.annualRate() < 0) 
            throw new InvalidCreditSimulationException("Interest rate cannot be negative");
    }
}