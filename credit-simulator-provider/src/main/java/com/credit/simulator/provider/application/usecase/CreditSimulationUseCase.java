package com.credit.simulator.provider.application.usecase;

import com.credit.simulator.provider.application.messagin.ProviderEventPublisher;
import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.model.shared.MathUtils;
import com.credit.simulator.provider.domain.repository.CreditSimulationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditSimulationUseCase {

	private static final Logger logger = LoggerFactory.getLogger(CreditSimulationUseCase.class);

	private final CreditSimulationRepository repository;
	private final ProviderEventPublisher eventPublisher;

	public CreditSimulationUseCase(CreditSimulationRepository repository, ProviderEventPublisher eventPublisher) {
		this.repository = repository;
		this.eventPublisher = eventPublisher;
	}

	public Mono<CreditSimulation> execute(String id) {
		return repository.findById(id).doOnSuccess(simulation -> logger.debug("Found simulation: {}", id))
				.doOnError(error -> logger.error("Error finding simulation: {}", id, error));
	}

	public Mono<List<CreditSimulation>> execute(List<CreditSimulation> simulations) {
		logger.info("Processing {} simulations in batches of 1000", simulations.size());

		return Flux.fromIterable(simulations).buffer(1000).flatMap(this::processBatch, 2).collectList()
				.doOnSuccess(creditSimulationresults -> logger.info("Completed processing {} simulations",
						creditSimulationresults.size()));
	}

	private Flux<CreditSimulation> processBatch(List<CreditSimulation> batch) {

		logger.info("Processing batch {} of {}", batch.hashCode(), batch.size());
		
		List<CreditSimulation> processedBatch = batch.stream()
			        .map(MathUtils::setDefaultValues)
			        .map(MathUtils::roundAllDecimals)
			        .collect(Collectors.toList());

		return repository.saveAll(processedBatch).collectList().flatMapMany(savedList -> {
			return Flux.fromIterable(savedList)
					.flatMap(saved -> eventPublisher.publishEvent(saved).then(Mono.just(saved)));
		});
	}

	public Flux<CreditSimulation> findAllSimulations() {
		logger.info("Retrieving all simulations");

		return repository.findAll().doOnComplete(() -> logger.info("Retrieved all simulations"))
				.doOnError(error -> logger.error("Error retrieving all simulations", error));
	}

	public Mono<Long> countSimulations() {
		logger.info("Counting total simulations");

		return repository.count().doOnSuccess(count -> logger.info("Total simulations count: {}", count))
				.doOnError(error -> logger.error("Error counting simulations", error));
	}

}