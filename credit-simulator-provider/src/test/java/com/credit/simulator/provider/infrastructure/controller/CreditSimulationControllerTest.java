package com.credit.simulator.provider.infrastructure.controller;

import com.credit.simulator.provider.application.usecase.CreditSimulationUseCase;
import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.enums.CreditSimulationStatusEnum;
import com.credit.simulator.provider.domain.model.enums.RateTypeEnum;
import com.credit.simulator.provider.domain.model.exception.InvalidCreditSimulationException;
import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.provider.domain.service.CurrencyService;
import com.credit.simulator.provider.infrastructure.dto.*;
import com.credit.simulator.provider.infrastructure.mapper.CreditSimulationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditSimulationControllerTest {

    @Mock
    private CreditSimulationUseCase creditSimulationUseCase;

    @Mock
    private CreditSimulationMapper mapper;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CreditSimulationController controller;

    private WebTestClient webTestClient;
    private CreditSimulation testSimulation;
    private CreditSimulationRequestDTO requestDTO;
    private CreditSimulationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();
        
        testSimulation = new CreditSimulation(
            "689ab7e9d9d99f0ed4c0aee1",
            1000.0,
            LocalDate.of(1990, 1, 1),
            12,
            CreditSimulationStatusEnum.PENDING,
            1200.0,
            100.0,
            200.0,
            2.0,
            RateTypeEnum.FIXED.toString(),
            CurrencyTypeEnum.BRL.toString(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        requestDTO = new CreditSimulationRequestDTO(
            1000.0,
            LocalDate.of(1990, 1, 1),
            12,
            RateTypeEnum.FIXED.toString(),
            CurrencyTypeEnum.BRL.toString()
        );

        responseDTO = new CreditSimulationResponseDTO(
            "689ab7e9d9d99f0ed4c0aee1",
            1000.0,
            LocalDate.of(1990, 1, 1),
            12,
            CreditSimulationStatusEnum.PENDING,
            1200.0,
            100.0,
            200.0,
            2.0,
            RateTypeEnum.FIXED.toString(),
            CurrencyTypeEnum.BRL.toString(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    // Testa criação de simulação em lote com sucesso
    @Test
    void createsBatchSimulationSuccessfully() {
        BatchCreditSimulationRequestDTO batchRequest = new BatchCreditSimulationRequestDTO(
            Arrays.asList(requestDTO, requestDTO)
        );
        List<CreditSimulation> domainSimulations = Arrays.asList(testSimulation, testSimulation);
        BatchCreditSimulationResponseDTO batchResponse = new BatchCreditSimulationResponseDTO(
            Arrays.asList(responseDTO, responseDTO), 2
        );

        when(mapper.toDomain(any(CreditSimulationRequestDTO.class))).thenReturn(testSimulation);
        when(creditSimulationUseCase.execute(anyList())).thenReturn(Mono.just(domainSimulations));
        when(mapper.toLargeResponseDTO(anyList())).thenReturn(batchResponse);

        webTestClient.post()
            .uri("/api/credit-simulation/proccess")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(batchRequest)
            .exchange()
            .expectStatus().isAccepted()
            .expectBody()
            .jsonPath("$.total").isEqualTo(2)
            .jsonPath("$.simulations").isArray();

        verify(mapper, times(2)).toDomain(any(CreditSimulationRequestDTO.class));
        verify(creditSimulationUseCase).execute(anyList());
        verify(mapper).toLargeResponseDTO(anyList());
    }

    // Testa erro durante criação em lote
    @Test
    void handlesUseCaseErrorDuringBatchCreation() {
        BatchCreditSimulationRequestDTO batchRequest = new BatchCreditSimulationRequestDTO(
            Arrays.asList(requestDTO)
        );
        
        InvalidCreditSimulationException error = new InvalidCreditSimulationException("Processing error");
        when(mapper.toDomain(any(CreditSimulationRequestDTO.class))).thenReturn(testSimulation);
        when(creditSimulationUseCase.execute(anyList())).thenReturn(Mono.error(error));

        StepVerifier.create(controller.createSimulation(batchRequest))
            .expectError(InvalidCreditSimulationException.class)
            .verify();

        verify(mapper).toDomain(any(CreditSimulationRequestDTO.class));
        verify(creditSimulationUseCase).execute(anyList());
    }

    // Testa busca de simulação por ID com conversão de moeda
    @Test
    void getsSimulationByIdWithCurrencyConversion() {
        CreditSimulationResponseDTO convertedResponse = new CreditSimulationResponseDTO(
            "689ab7e9d9d99f0ed4c0aee1", 
            180.0, 
            LocalDate.of(1990, 1, 1), 
            12, 
            CreditSimulationStatusEnum.PENDING, 
            216.0, 
            18.0, 
            36.0, 
            2.0, 
            RateTypeEnum.FIXED.toString(), 
            CurrencyTypeEnum.USD.toString(), 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );
        
        when(creditSimulationUseCase.execute("689ab7e9d9d99f0ed4c0aee1")).thenReturn(Mono.just(testSimulation));
        when(mapper.toResponseDTO(testSimulation)).thenReturn(responseDTO);
        when(currencyService.convert(responseDTO, CurrencyTypeEnum.USD.toString())).thenReturn(convertedResponse);

        webTestClient.get()
            .uri("/api/credit-simulation/689ab7e9d9d99f0ed4c0aee1?currency=USD")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo("689ab7e9d9d99f0ed4c0aee1")
            .jsonPath("$.loanAmount").isEqualTo(180.0)
            .jsonPath("$.currency").isEqualTo(CurrencyTypeEnum.USD.toString());

        verify(creditSimulationUseCase).execute("689ab7e9d9d99f0ed4c0aee1");
        verify(mapper).toResponseDTO(testSimulation);
        verify(currencyService).convert(responseDTO, CurrencyTypeEnum.USD.toString());
    }


    // Testa erro de conversão de moeda
    @Test
    void handlesCurrencyConversionError() {
    	InvalidCreditSimulationException conversionError = new InvalidCreditSimulationException("Currency conversion failed");
        
        when(creditSimulationUseCase.execute("689ab7e9d9d99f0ed4c0aee1")).thenReturn(Mono.just(testSimulation));
        when(mapper.toResponseDTO(testSimulation)).thenReturn(responseDTO);
        when(currencyService.convert(responseDTO, CurrencyTypeEnum.USD.toString())).thenThrow(conversionError);

        StepVerifier.create(controller.getSimulation("689ab7e9d9d99f0ed4c0aee1", CurrencyTypeEnum.USD.toString()))
            .expectError(InvalidCreditSimulationException.class)
            .verify();

        verify(creditSimulationUseCase).execute("689ab7e9d9d99f0ed4c0aee1");
        verify(mapper).toResponseDTO(testSimulation);
        verify(currencyService).convert(responseDTO, CurrencyTypeEnum.USD.toString());
    }

    // Testa erro ao obter contagem
    @Test
    void handlesUseCaseErrorWhenGettingCount() {
        InvalidCreditSimulationException error = new InvalidCreditSimulationException("Database error");
        when(creditSimulationUseCase.countSimulations()).thenReturn(Mono.error(error));

        StepVerifier.create(controller.getSimulationsCount())
            .expectError(InvalidCreditSimulationException.class)
            .verify();

        verify(creditSimulationUseCase).countSimulations();
    }

    // Testa busca de todas as simulações com sucesso
    @Test
    void getsAllSimulationsSuccessfully() {
        List<CreditSimulation> simulations = Arrays.asList(testSimulation, testSimulation);
        
        when(creditSimulationUseCase.findAllSimulations()).thenReturn(Flux.fromIterable(simulations));
        when(mapper.toResponseDTO(any(CreditSimulation.class))).thenReturn(responseDTO);

        webTestClient.get()
            .uri("/api/credit-simulation/find-all")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(CreditSimulationResponseDTO.class)
            .hasSize(2);

        verify(creditSimulationUseCase).findAllSimulations();
        verify(mapper, times(2)).toResponseDTO(any(CreditSimulation.class));
    }
}