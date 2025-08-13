package com.credit.simulator.provider.domain.repository;

import com.credit.simulator.provider.domain.model.CreditSimulation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CreditSimulationRepository {
 
    Flux<CreditSimulation> saveAll(List<CreditSimulation> simulations);
    Mono<CreditSimulation> findById(String id);
    Mono<Long> count();
    Flux<CreditSimulation> findAll();

}