package com.ryazangrove.ratesexchangeandssnvalidator.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class SsnValidatorResponse {

    private boolean ssn_valid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error_Message;

    public SsnValidatorResponse(boolean ssnIsValid, String errorMessage) {
        this.ssn_valid = ssnIsValid;
        this.error_Message = errorMessage;
    }

    public SsnValidatorResponse(boolean ssnIsValid) {
        this.ssn_valid = ssnIsValid;
    }
}
