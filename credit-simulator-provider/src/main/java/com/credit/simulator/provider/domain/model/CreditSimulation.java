package com.credit.simulator.provider.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        this.totalInterestRate = totalInterestRate;
        this.interestRate = interestRate;
        this.interestRateType = interestRateType;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static CreditSimulation createPending(Double loanAmount, LocalDate birthDate, Integer paymentTermMonths, String interestType, String currency) {
        return new CreditSimulation(null, loanAmount, birthDate, paymentTermMonths, 
            CreditSimulationStatusEnum.PENDING, null, null, null, null, interestType, currency,
            LocalDateTime.now(), LocalDateTime.now());
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
    
    public Double getTotalInterestRate() { return totalInterestRate; }
    public void setTotalInterestRate(Double totalInterestRate) { this.totalInterestRate = totalInterestRate; }
    
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
	public String getInterestRateType() {return interestRateType;}
	public void setInterestRateType(String interestRateType) { this.interestRateType = interestRateType; }

	public String getCurrency() {return currency;}

	public void setCurrency(String currency) {this.currency = currency;}
}