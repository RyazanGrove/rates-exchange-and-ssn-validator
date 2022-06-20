package com.ryazangrove.ratesexchangeandssnvalidator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryazangrove.ratesexchangeandssnvalidator.models.ExchangeRates;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class RatesExchangeService {
    public static String apiKey;
    public static Set<String> supportedCurrencies = new HashSet<>(){{
        add("EUR");
        add("USD");
        add("SEK");
    }};
    public static Map<String, Double> exchangeRatesMap;
    public static String EXCHANGE_BASE = "EUR";
    public static String ERROR_MESSAGE_INCORRECT_API_KEY = "401 Unauthorized: \"{\"message\":\"Invalid authentication credentials\"}\"";
    public static boolean apiKeyIsIncorrect = false;


    public static void updateExchangeRates() {
        if(apiKey != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", apiKey);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.apilayer.com/exchangerates_data/latest")
                    .queryParam("symbols", String.join(",", supportedCurrencies))
                    .queryParam("base", EXCHANGE_BASE);

            HttpEntity entity = new HttpEntity(headers);
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<ExchangeRates> response = restTemplate.exchange(
                        builder.build().toString(),
                        HttpMethod.GET,
                        entity,
                        ExchangeRates.class);

                exchangeRatesMap = parseRates(response.getBody().rates);
            } catch (HttpClientErrorException e) {
                System.out.println(e);
                if(e.getMessage().equals(ERROR_MESSAGE_INCORRECT_API_KEY)){
                    apiKeyIsIncorrect = true;
                }
            }
        }
    }

    public static Map<String, Double> parseRates(ExchangeRates.Rates exchangeRates) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Double> mappedExchangeRates = oMapper.convertValue(exchangeRates, Map.class);

        Map<String, Double> rates = new HashMap<>();
        for(String currency1 : RatesExchangeService.supportedCurrencies){
            for(String currency2 : RatesExchangeService.supportedCurrencies){
                rates.put(currency1 + "-" + currency2, mappedExchangeRates.get(currency2) / mappedExchangeRates.get(currency1));
            }
        }

        return rates;
    }
}
