package com.ryazangrove.ratesexchangeandssnvalidator.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatesExchangeControllerUnitTest {

    @Test
    void checkRequestParameterErrorsNullAndEmptyValue() {
        assertEquals(RatesExchangeController.ERROR_CURRENCY_IS_EMPTY, RatesExchangeController.checkRequestParameterErrors(null, "EUR"), "RatesExchangeController.checkRequestParameterErrors() should handle null value");
        assertEquals(RatesExchangeController.ERROR_CURRENCY_IS_EMPTY, RatesExchangeController.checkRequestParameterErrors("", "EUR"), "RatesExchangeController.checkRequestParameterErrors() should handle null value");
        assertEquals(RatesExchangeController.ERROR_CURRENCY_IS_EMPTY, RatesExchangeController.checkRequestParameterErrors("USD", null), "RatesExchangeController.checkRequestParameterErrors() should handle null value");
        assertEquals(RatesExchangeController.ERROR_CURRENCY_IS_EMPTY, RatesExchangeController.checkRequestParameterErrors("USD", ""), "RatesExchangeController.checkRequestParameterErrors() should handle null value");
    }

    @Test
    void checkRequestParameterErrorsCorrectValue() {
        assertEquals(RatesExchangeController.CORRECT, RatesExchangeController.checkRequestParameterErrors("USD", "EUR"), "RatesExchangeController.checkRequestParameterErrors() should handle corrects values");
        assertEquals(RatesExchangeController.CORRECT, RatesExchangeController.checkRequestParameterErrors("EUR", "SEK"), "RatesExchangeController.checkRequestParameterErrors() should handle corrects values");
    }

    @Test
    void verifyRequestParametersCheckForSupportedCurrencies() {
        assertEquals(RatesExchangeController.ERROR_CURRENCY_IS_NOT_SUPPORTED, RatesExchangeController.verifyRequestParameters("RUB", "EUR", 100.2), "RatesExchangeController.verifyRequestParameters() should check supported currencies");
        assertEquals(RatesExchangeController.ERROR_CURRENCY_IS_NOT_SUPPORTED, RatesExchangeController.verifyRequestParameters("EUR", "JPY", 100.2), "RatesExchangeController.verifyRequestParameters() should check supported currencies");
        }

    @Test
    void verifyRequestParametersCheckForNegativeAmount() {
        assertEquals(RatesExchangeController.ERROR_REQUESTED_AMOUNT_IS_NEGATIVE, RatesExchangeController.verifyRequestParameters("USD", "EUR", -10.54), "RatesExchangeController.verifyRequestParameters() should check negative amount");
    }

    @Test
    void verifyRequestParametersCorrectValue() {
        assertEquals(RatesExchangeController.CORRECT, RatesExchangeController.verifyRequestParameters("USD", "EUR", 200), "RatesExchangeController.verifyRequestParameters() should handle correct values");
        assertEquals(RatesExchangeController.CORRECT, RatesExchangeController.verifyRequestParameters("EUR", "SEK", 80.5), "RatesExchangeController.verifyRequestParameters() should handle correct values");
        assertEquals(RatesExchangeController.CORRECT, RatesExchangeController.verifyRequestParameters("SEK", "USD", 1234.89), "RatesExchangeController.verifyRequestParameters() should handle correct values");
    }
}