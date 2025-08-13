package com.credit.simulator.consumer.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.credit.simulator.consumer.domain.model.CreditSimulation;

@Repository
public interface ReactiveMongoCreditSimulationRepository 
       extends ReactiveMongoRepository<CreditSimulation, String> {
}