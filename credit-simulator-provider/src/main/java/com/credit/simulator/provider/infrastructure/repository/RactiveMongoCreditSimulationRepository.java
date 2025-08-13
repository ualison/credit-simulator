package com.credit.simulator.provider.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.credit.simulator.provider.domain.model.CreditSimulation;

@Repository
public interface RactiveMongoCreditSimulationRepository 
       extends ReactiveMongoRepository<CreditSimulation, String> {
}