package com.credit.simulator.provider.infrastructure.controller;

import com.credit.simulator.provider.application.usecase.CreditSimulationUseCase;
import com.credit.simulator.provider.domain.service.CurrencyService;
import com.credit.simulator.provider.infrastructure.dto.*;
import com.credit.simulator.provider.infrastructure.mapper.CreditSimulationMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/credit-simulation")
@Tag(name = "Credit Simulation")
public class CreditSimulationController {

	private final CreditSimulationUseCase creditSimulationUseCase;
	private final CreditSimulationMapper mapper;
	private final CurrencyService currencyService;

	public CreditSimulationController(CreditSimulationUseCase processingCreditSimulationUseCase,
			CreditSimulationMapper mapper, CurrencyService currencyService) {
		this.creditSimulationUseCase = processingCreditSimulationUseCase;
		this.mapper = mapper;
		this.currencyService = currencyService;
	}

	@PostMapping("/proccess")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<BatchCreditSimulationResponseDTO> createSimulation(
			@Valid @RequestBody BatchCreditSimulationRequestDTO request) {
		return creditSimulationUseCase.execute(request.simulations().stream().map(mapper::toDomain).toList())
				.map(mapper::toLargeResponseDTO);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<CreditSimulationResponseDTO> getSimulation(@PathVariable String id,
			@RequestParam(defaultValue = "BRL") String currency) {
		return creditSimulationUseCase.execute(id).map(mapper::toResponseDTO)
				.map(dto -> currencyService.convert(dto, currency));
	}

	@GetMapping("/count-all")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Long> getSimulationsCount() {
		return creditSimulationUseCase.countSimulations();
	}

	@GetMapping("/find-all")
	@ResponseStatus(HttpStatus.OK)
	public Flux<CreditSimulationResponseDTO> getAllSimulations() {
		return creditSimulationUseCase.findAllSimulations().map(mapper::toResponseDTO);
	}
}