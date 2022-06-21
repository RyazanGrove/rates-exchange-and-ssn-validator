package com.ryazangrove.ratesexchangeandssnvalidator.controllers;

import com.ryazangrove.ratesexchangeandssnvalidator.models.ExchangeAmountResponse;
import com.ryazangrove.ratesexchangeandssnvalidator.services.RatesExchangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class RatesExchangeController {

    public static final String CORRECT = "OK";
    public static final String ERROR_CURRENCY_IS_EMPTY = "error: currency is empty";
    public static final String ERROR_REQUESTED_AMOUNT_IS_EMPTY = "error: currency is empty";
    public static final String ERROR_CURRENCY_IS_NOT_SUPPORTED = "error: currency is not supported";
    public static final String ERROR_AMOUNT_INCORRECT_FORMAT = "error: currency incorrect format";
    public static final String ERROR_REQUESTED_AMOUNT_IS_NEGATIVE = "error: requested amount should not be negative";
    public static final String ERROR_SERVICE_DATA_UPDATE_ERROR = "error: service data update error";
    public static final String ERROR_API_KEY_IS_REQUIRED = "error: api key is required";
    public static final String ERROR_API_KEY_IS_INCORRECT = "error: api key is incorrect";

    @GetMapping("/exchange_amount")
    public ExchangeAmountResponse exchangeAmount(@RequestParam Map<String,String> requestParams){
        String from = requestParams.get("from");
        String to = requestParams.get("to");
        double from_amount;

        if(RatesExchangeService.apiKey == null){
            ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                    .error_Message(ERROR_API_KEY_IS_REQUIRED)
                    .build();
            return response;
        }

        if(RatesExchangeService.apiKeyIsIncorrect){
            ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                    .error_Message(ERROR_API_KEY_IS_INCORRECT)
                    .build();
            return response;
        }

        String errorMessage = checkRequestParameterErrors(from, to);
        if(!errorMessage.equals(CORRECT)){
            ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                    .error_Message(errorMessage)
                    .build();
            return response;
        }

        try {
            if(requestParams.get("from_amount") != null) {
                from_amount = Double.parseDouble(requestParams.get("from_amount"));
            } else {
                ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                        .error_Message(ERROR_REQUESTED_AMOUNT_IS_EMPTY)
                        .build();
                return response;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                    .error_Message(ERROR_AMOUNT_INCORRECT_FORMAT)
                    .build();
            return response;
        }

        String verifyMessage = verifyRequestParameters(from, to, from_amount);
        if(!verifyMessage.equals(CORRECT)){
            ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                    .error_Message(verifyMessage)
                    .build();
            return response;
        }

        double exchangeRate;
        if(RatesExchangeService.exchangeRatesMap != null){
            exchangeRate = RatesExchangeService.exchangeRatesMap.get(from + "-" + to);
        } else {
            ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                    .error_Message(ERROR_SERVICE_DATA_UPDATE_ERROR)
                    .build();
            return response;
        }
        double exchangeAmount = exchangeRate * from_amount;

        ExchangeAmountResponse response = ExchangeAmountResponse.builder()
                .from(from)
                .to(to)
                .exchange_rate(exchangeRate)
                .to_amount(exchangeAmount)
                .build();

        return response;
    }

    public static String checkRequestParameterErrors(String from, String to){
        if(from == null || from.isEmpty() || to == null || to.isEmpty()){
            return ERROR_CURRENCY_IS_EMPTY;
        }
        return CORRECT;
    }

    public static String verifyRequestParameters(String from, String to, double amount) {
        if(!RatesExchangeService.supportedCurrencies.contains(from) || !RatesExchangeService.supportedCurrencies.contains(to)) {
            return ERROR_CURRENCY_IS_NOT_SUPPORTED;
        }
        if(amount < 0) {
            return ERROR_REQUESTED_AMOUNT_IS_NEGATIVE;
        }
        return CORRECT;
    }
}
