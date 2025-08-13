package com.credit.simulator.provider.application.messagin;

import com.credit.simulator.provider.domain.model.CreditSimulation;

import reactor.core.publisher.Mono;

public interface ProviderEventPublisher {
	 Mono<Void> publishEvent(CreditSimulation simulation);
}
