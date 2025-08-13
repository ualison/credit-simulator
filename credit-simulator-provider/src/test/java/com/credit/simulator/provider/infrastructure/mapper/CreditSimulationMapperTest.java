package com.credit.simulator.provider.infrastructure.mapper;

import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.provider.domain.model.enums.RateTypeEnum;
import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.provider.infrastructure.dto.CreditSimulationRequestDTO;
import com.credit.simulator.provider.infrastructure.dto.CreditSimulationResponseDTO;
import com.credit.simulator.provider.infrastructure.dto.BatchCreditSimulationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditSimulationMapperTest {

    private CreditSimulationMapper mapper;
    private CreditSimulationRequestDTO requestDTO;
    private CreditSimulation domainSimulation;

    @BeforeEach
    void setUp() {
        mapper = new CreditSimulationMapper();
        
        requestDTO = new CreditSimulationRequestDTO(
            1000.0, LocalDate.of(1990, 1, 1), 12, RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString()
        );
        
        domainSimulation = new CreditSimulation(
            "689ab7e9d9d99f0ed4c0aee1", 1000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.PENDING, 1200.0, 100.0, 200.0, 2.0, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );
    }

    // Mapeia request DTO para objeto de domínio com status pendente
    @Test
    void mapsRequestDtoToDomainWithPendingStatus() {
        CreditSimulation result = mapper.toDomain(requestDTO);
        
        assertNull(result.getId());
        assertEquals(requestDTO.loanAmount(), result.getLoanAmount());
        assertEquals(requestDTO.birthDate(), result.getBirthDate());
        assertEquals(requestDTO.paymentTermMonths(), result.getPaymentTermMonths());
        assertEquals(CreditSimulationStatusEnum.PENDING, result.getStatus());
        assertNull(result.getTotalAmount());
        assertNull(result.getMonthlyPayment());
        assertNull(result.getTotalInterestRate());
        assertNull(result.getInterestRate());
        assertEquals(requestDTO.interestRateType(), result.getInterestRateType());
        assertEquals(requestDTO.currency(), result.getCurrency());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    // Mapeia objeto de domínio para response DTO com todos os campos
    @Test
    void mapsDomainToResponseDtoWithAllFields() {
        CreditSimulationResponseDTO result = mapper.toResponseDTO(domainSimulation);
        
        assertEquals(domainSimulation.getId(), result.id());
        assertEquals(domainSimulation.getLoanAmount(), result.loanAmount());
        assertEquals(domainSimulation.getBirthDate(), result.birthDate());
        assertEquals(domainSimulation.getPaymentTermMonths(), result.paymentTermMonths());
        assertEquals(domainSimulation.getStatus(), result.status());
        assertEquals(domainSimulation.getTotalAmount(), result.totalAmount());
        assertEquals(domainSimulation.getMonthlyPayment(), result.monthlyPayment());
        assertEquals(domainSimulation.getTotalInterestRate(), result.totalInterestRate());
        assertEquals(domainSimulation.getInterestRate(), result.interestRate());
        assertEquals(domainSimulation.getInterestRateType(), result.interestRateType());
        assertEquals(domainSimulation.getCurrency(), result.currency());
        assertEquals(domainSimulation.getCreatedAt(), result.createdAt());
        assertEquals(domainSimulation.getUpdatedAt(), result.updatedAt());
    }

    // Mapeia lista de objetos de domínio para batch response DTO
    @Test
    void mapsListOfDomainsToBatchResponseDto() {
        List<CreditSimulation> simulations = Arrays.asList(domainSimulation, domainSimulation);
        
        BatchCreditSimulationResponseDTO result = mapper.toLargeResponseDTO(simulations);
        
        assertEquals(2, result.simulations().size());
        assertEquals(2, result.total());
        assertEquals(domainSimulation.getId(), result.simulations().get(0).id());
        assertEquals(domainSimulation.getId(), result.simulations().get(1).id());
    }


    // Mapeia domínio com status completado para response DTO
    @Test
    void mapsDomainWithCompletedStatusToResponseDto() {
        CreditSimulation completedSimulation = new CreditSimulation(
            "completed-id", 1000.0, LocalDate.of(1990, 1, 1), 12, 
            CreditSimulationStatusEnum.COMPLETED, 1200.0, 100.0, 200.0, 2.0, 
            RateTypeEnum.FIXED.toString(), CurrencyTypeEnum.BRL.toString(), LocalDateTime.now(), LocalDateTime.now()
        );
        
        CreditSimulationResponseDTO result = mapper.toResponseDTO(completedSimulation);
        
        assertEquals(CreditSimulationStatusEnum.COMPLETED, result.status());
        assertEquals("completed-id", result.id());
    }
}