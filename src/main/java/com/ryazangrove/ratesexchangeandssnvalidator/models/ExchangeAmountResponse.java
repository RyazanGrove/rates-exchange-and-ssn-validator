package com.ryazangrove.ratesexchangeandssnvalidator.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeAmountResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Double to_amount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Double exchange_rate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String errorMessage;
}
