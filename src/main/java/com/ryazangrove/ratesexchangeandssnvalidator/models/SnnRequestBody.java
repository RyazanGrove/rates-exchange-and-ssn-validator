package com.ryazangrove.ratesexchangeandssnvalidator.models;

import lombok.Data;

@Data
public class SnnRequestBody {

    private String ssn;

    private String country_code;

    public SnnRequestBody(String ssn, String country_code) {
        this.ssn = ssn;
        this.country_code = country_code;
    }
}
