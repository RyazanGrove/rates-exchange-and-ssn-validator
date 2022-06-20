package com.ryazangrove.ratesexchangeandssnvalidator.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExchangeRates {
    public String base;
    public String date;
    public boolean success;
    public long timestamp;
    public Rates rates;

    @Data
    public static class Rates {
        @JsonProperty("EUR")
        public double eur;
        @JsonProperty("USD")
        public double usd;
        @JsonProperty("SEK")
        public double sek;
    }
}
