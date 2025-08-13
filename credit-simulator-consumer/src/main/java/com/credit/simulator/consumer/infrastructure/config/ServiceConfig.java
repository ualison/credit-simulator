package com.credit.simulator.consumer.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.credit.simulator.consumer.domain.service.CreditCalculationService;

@Configuration
public class ServiceConfig {
    
    @Bean
    CreditCalculationService creditCalculationService() {
        return new CreditCalculationService();
    }
    
}