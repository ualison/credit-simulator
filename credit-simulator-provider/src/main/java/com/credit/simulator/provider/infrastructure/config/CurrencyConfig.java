package com.credit.simulator.provider.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.credit.simulator.provider.domain.service.CurrencyService;
import com.credit.simulator.provider.infrastructure.factory.CurrencyConverterFactory;

@Configuration
public class CurrencyConfig {

	  @Bean
	    CurrencyConverterFactory currencyConverterFactory() {
	        return new CurrencyConverterFactory();
	    }
	    
	    @Bean
	    CurrencyService currencyService(CurrencyConverterFactory factory) {
	        return new CurrencyService(factory);
	    }
}