package com.credit.simulator.consumer.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.credit.simulator.consumer.domain.model.enums.CreditSimulationStatusEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "credit_simulations")
public class CreditSimulation {
    @Id
    private String id;
    private Double loanAmount;
    private LocalDate birthDate;
    private Integer paymentTermMonths;
    private CreditSimulationStatusEnum status;
    private Double totalAmount;
    private Double monthlyPayment;
    private Double totalInterestRate;
    private Double interestRate;
    private String interestRateType;
    private String currency;
	private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreditSimulation() {}

    @JsonCreator
    public CreditSimulation(
            @JsonProperty("id") String id,
            @JsonProperty("loanAmount") Double loanAmount,
            @JsonProperty("birthDate") LocalDate birthDate,
            @JsonProperty("paymentTermMonths") Integer paymentTermMonths,
            @JsonProperty("status") CreditSimulationStatusEnum status,
            @JsonProperty("totalAmount") Double totalAmount,
            @JsonProperty("monthlyPayment") Double monthlyPayment,
            @JsonProperty("totalInterestRate") Double totalInterestRate,
            @JsonProperty("interestRate") Double interestRate,
            @JsonProperty("interestRateType") String interestRateType,
            @JsonProperty("currency") String currency,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.birthDate = birthDate;
        this.paymentTermMonths = paymentTermMonths;
        this.status = status;
        this.totalAmount = totalAmount;
        this.monthlyPayment = monthlyPayment;
        this.setTotalInterestRate(totalInterestRate);
        this.interestRate = interestRate;
        this.interestRateType = interestRateType;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public CreditSimulation withCalculationResult(Double totalAmount, Double monthlyPayment,
            Double totalInterestRate, Double interestRate) {
        return new CreditSimulation(id, loanAmount, birthDate, paymentTermMonths, 
            CreditSimulationStatusEnum.COMPLETED, totalAmount, monthlyPayment, totalInterestRate, 
            interestRate, interestRateType, currency, createdAt, LocalDateTime.now());
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public Integer getPaymentTermMonths() { return paymentTermMonths; }
    public void setPaymentTermMonths(Integer paymentTermMonths) { this.paymentTermMonths = paymentTermMonths; }
    
    public CreditSimulationStatusEnum getStatus() { return status; }
    public void setStatus(CreditSimulationStatusEnum status) { this.status = status; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public Double getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(Double monthlyPayment) { this.monthlyPayment = monthlyPayment; }
    
    public void setTotalInterest(Double totalInterestRate) { this.setTotalInterestRate(totalInterestRate); }
    
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
    
    public String getInterestRateType() { return interestRateType; }
    public void setInterestRateType(String interestRateType) { this.interestRateType = interestRateType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public Double getTotalInterestRate() {return totalInterestRate;}

	public void setTotalInterestRate(Double totalInterestRate) {this.totalInterestRate = totalInterestRate;}
}