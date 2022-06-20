package com.ryazangrove.ratesexchangeandssnvalidator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryazangrove.ratesexchangeandssnvalidator.models.ExchangeAmountResponse;
import com.ryazangrove.ratesexchangeandssnvalidator.services.RatesExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@WebMvcTest(RatesExchangeController.class)
class RatesExchangeControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void validateExchangeAmountEndpoint() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("EUR")));
            put("to", new ArrayList<String>(Arrays.asList("USD")));
            put("amount", new ArrayList<String>(Arrays.asList("100")));
        }};
        Map<String, Double> exchangeRatesMap = new HashMap<String, Double>(){{
            put("EUR-USD", 1.04);
        }};
        RatesExchangeService.exchangeRatesMap = exchangeRatesMap;
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .from("EUR")
                .to("USD")
                .to_amount(104.)
                .exchange_rate(1.04)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return response with exchange rate");
    }

    @Test
    void validateExchangeAmountNoLoadedData() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("EUR")));
            put("to", new ArrayList<String>(Arrays.asList("USD")));
            put("amount", new ArrayList<String>(Arrays.asList("100.2")));
        }};
        RatesExchangeService.exchangeRatesMap = null;
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_SERVICE_DATA_UPDATE_ERROR)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. Not data loaded");
    }

    @Test
    void validateExchangeAmountRequestedCurrencyIsEmpty() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("")));
            put("to", new ArrayList<String>(Arrays.asList("USD")));
            put("amount", new ArrayList<String>(Arrays.asList("100.2")));
        }};
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_CURRENCY_IS_EMPTY)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. Currency is empty");
    }

    @Test
    void validateExchangeAmountRequestParameterCurrencyIsMissing() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("EUR")));
            put("amount", new ArrayList<String>(Arrays.asList("100.2")));
        }};
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_CURRENCY_IS_EMPTY)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. Currency is missing");
    }

    @Test
    void validateExchangeAmountRequestCurrencyIsNotSupported() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("RUB")));
            put("to", new ArrayList<String>(Arrays.asList("EUR")));
            put("amount", new ArrayList<String>(Arrays.asList("100.2")));
        }};
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_CURRENCY_IS_NOT_SUPPORTED)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. Requested currency is not supported");
    }

    @Test
    void validateExchangeAmountRequestedAmountIsNegative() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("EUR")));
            put("to", new ArrayList<String>(Arrays.asList("USD")));
            put("amount", new ArrayList<String>(Arrays.asList("-10")));
        }};
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_REQUESTED_AMOUNT_IS_NEGATIVE)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. Requested amount is negative");
    }

    @Test
    void validateExchangeAmountRequestAmountIncorrectFormat() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("USD")));
            put("to", new ArrayList<String>(Arrays.asList("EUR")));
            put("amount", new ArrayList<String>(Arrays.asList("1a5b8.85")));
        }};
        RatesExchangeService.apiKey = "apiKey";
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_AMOUNT_INCORRECT_FORMAT)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. Requested amount is in incorrect format");
    }

    @Test
    void validateExchangeAmountApiKeyIsEmpty() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>(){{
            put("from", new ArrayList<String>(Arrays.asList("USD")));
            put("to", new ArrayList<String>(Arrays.asList("EUR")));
            put("amount", new ArrayList<String>(Arrays.asList("85.85")));
        }};
        RatesExchangeService.apiKey = null;
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.get("/exchange_amount").queryParams(paramsMap);
        MvcResult result = mvc.perform(request).andReturn();

        ExchangeAmountResponse expectedResponse = ExchangeAmountResponse.builder()
                .errorMessage(RatesExchangeController.ERROR_API_KEY_IS_REQUIRED)
                .build();
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "RatesExchangeController.exchangeAmount() should return error. API Key is required");
    }
}