package com.credit.simulator.consumer.infrastructure.repository;

import com.credit.simulator.consumer.domain.model.CreditSimulation;
import com.credit.simulator.consumer.domain.repository.CreditSimulationRepository;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
public class MongoCreditSimulationRepository implements CreditSimulationRepository {

    private final ReactiveMongoCreditSimulationRepository springRepo;

    public MongoCreditSimulationRepository(ReactiveMongoCreditSimulationRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Mono<CreditSimulation> update(CreditSimulation simulation) {
        return springRepo.save(simulation);
    }

    @Override
    public Mono<CreditSimulation> findById(String id) {
        return springRepo.findById(id);
    }
}