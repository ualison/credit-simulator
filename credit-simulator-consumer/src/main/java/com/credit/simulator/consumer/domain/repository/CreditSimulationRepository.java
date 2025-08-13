package com.credit.simulator.consumer.domain.repository;

import com.credit.simulator.consumer.domain.model.CreditSimulation;

import reactor.core.publisher.Mono;

public interface CreditSimulationRepository {
    Mono<CreditSimulation> update(CreditSimulation simulation);
    Mono<CreditSimulation> findById(String id);
}