package com.credit.simulator.provider.infrastructure.repository;

import com.credit.simulator.provider.domain.model.CreditSimulation;
import com.credit.simulator.provider.domain.repository.CreditSimulationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class MongoCreditSimulationRepository implements CreditSimulationRepository {

    private static final Logger logger = LoggerFactory.getLogger(MongoCreditSimulationRepository.class);
    
    private final RactiveMongoCreditSimulationRepository springRepo;
    public MongoCreditSimulationRepository(RactiveMongoCreditSimulationRepository springRepo, 
                                         ReactiveMongoTemplate mongoTemplate) {
        this.springRepo = springRepo;
    }

    @Override
    public Flux<CreditSimulation> saveAll(List<CreditSimulation> simulations) {
        logger.info("Saving {} simulations", simulations.size());
        return springRepo.saveAll(simulations)
                .doOnComplete(() -> logger.info("Save completed for {} simulations", simulations.size()))
                .doOnError(error -> logger.error("Error in save", error));
    }

    @Override
    public Mono<CreditSimulation> findById(String id) {
        return springRepo.findById(id)
                .doOnError(error -> logger.error("Error finding simulation by id: {}", id, error));
    }
    
    @Override
    public Mono<Long> count() {
        return springRepo.count()
                .doOnSuccess(count -> logger.debug("Total simulations count: {}", count))
                .doOnError(error -> logger.error("Error counting simulations", error));
    }
    
    @Override
    public Flux<CreditSimulation> findAll() {
        return springRepo.findAll()
                .doOnError(error -> logger.error("Error finding all simulations", error));
    }
    
}