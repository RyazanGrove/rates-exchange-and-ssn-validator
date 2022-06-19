package com.ryazangrove.ratesexchangeandssnvalidator;

import com.ryazangrove.ratesexchangeandssnvalidator.services.RatesExchangeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RatesExchangeAndSsnValidatorApplication {

	public static void main(String[] args) {
		RatesExchangeService.apiKey = args[0];
		SpringApplication.run(RatesExchangeAndSsnValidatorApplication.class, args);
	}
}
