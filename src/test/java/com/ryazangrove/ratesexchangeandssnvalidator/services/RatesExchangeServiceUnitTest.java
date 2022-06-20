package com.ryazangrove.ratesexchangeandssnvalidator.services;

import com.ryazangrove.ratesexchangeandssnvalidator.models.ExchangeRates;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RatesExchangeServiceUnitTest {

    @Test
    void parseRates() {
        double rateEur = 1.;
        double rateUsd = 1.04;
        double rateSek = 5.;

        ExchangeRates.Rates r = new ExchangeRates.Rates();
        r.setEur(rateEur);
        r.setUsd(rateUsd);
        r.setSek(rateSek);

        Map<String, Double> parsedRates = RatesExchangeService.parseRates(r);
        assertEquals(1, parsedRates.get("EUR-EUR") , "RatesExchangeService.parseRates() should provide 1 on the same currency");
        assertEquals(1, parsedRates.get("USD-USD") , "RatesExchangeService.parseRates() should provide 1 on the same currency");
        assertEquals(1, parsedRates.get("SEK-SEK") , "RatesExchangeService.parseRates() should provide 1 on the same currency");

        assertEquals(rateUsd / rateEur, parsedRates.get("EUR-USD") , "RatesExchangeService.parseRates() should provide 1.04 on the EUR-USD rate");
        assertEquals(rateEur / rateUsd, parsedRates.get("USD-EUR") , "RatesExchangeService.parseRates() should provide ~0.96 on the USD-EUR rate");

        assertEquals(rateSek / rateEur, parsedRates.get("EUR-SEK") , "RatesExchangeService.parseRates() should provide 5 on the EUR-SEK rate");
        assertEquals(rateEur / rateSek, parsedRates.get("SEK-EUR") , "RatesExchangeService.parseRates() should provide 0.2 on the SEK-EUR rate");

        assertEquals(rateSek / rateUsd, parsedRates.get("USD-SEK") , "RatesExchangeService.parseRates() should provide 4.80 on the USD-SEK rate");
        assertEquals(rateUsd / rateSek, parsedRates.get("SEK-USD") , "RatesExchangeService.parseRates() should provide 0.208 on the SEK-USD rate");
    }
}