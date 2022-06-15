package com.ryazangrove.ratesexchangeandssnvalidator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryazangrove.ratesexchangeandssnvalidator.models.SnnRequestBody;
import com.ryazangrove.ratesexchangeandssnvalidator.models.SsnValidatorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@WebMvcTest(SsnValidatorController.class)
class SsnValidatorControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void validateSsnEndpoint() throws Exception {
        String ssn = "030390-709E";
        String countryCode = SsnValidatorController.COUNTRY_CODE_FINLAND;
        SnnRequestBody requestBody = new SnnRequestBody(ssn,countryCode);
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.post("/validate_ssn").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody));
        MvcResult result = mvc.perform(request).andReturn();

        SsnValidatorResponse expectedResponse = new SsnValidatorResponse(true);
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "SsnValidatorController.validateSsnEndpoint() should return true");
    }

    @Test
    void validateSsnEndpointNotValidSsn() throws Exception {
        String ssn = "120478-123T";
        String countryCode = SsnValidatorController.COUNTRY_CODE_FINLAND;
        SnnRequestBody requestBody = new SnnRequestBody(ssn,countryCode);
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.post("/validate_ssn").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody));
        MvcResult result = mvc.perform(request).andReturn();

        SsnValidatorResponse expectedResponse = new SsnValidatorResponse(false);
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "SsnValidatorController.validateSsnEndpoint() should return false. Ssn is not valid");
    }

    @Test
    void validateSsnEndpointIncorrectCountryCode() throws Exception {
        String ssn = "030390-709E";
        String countryCode = "SE";
        SnnRequestBody requestBody = new SnnRequestBody(ssn,countryCode);
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.post("/validate_ssn").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody));
        MvcResult result = mvc.perform(request).andReturn();

        SsnValidatorResponse expectedResponse = new SsnValidatorResponse(false, SsnValidatorController.ERROR_COUNTRY_CODE_IS_NOT_SUPPORTED);
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "SsnValidatorController.validateSsnEndpoint() should return false and error that country code is not supported");
    }

    @Test
    void validateSsnEndpointSsnIsEmpty() throws Exception {
        String ssn = "";
        String countryCode = SsnValidatorController.COUNTRY_CODE_FINLAND;
        SnnRequestBody requestBody = new SnnRequestBody(ssn,countryCode);
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder request = MockMvcRequestBuilders.post("/validate_ssn").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody));
        MvcResult result = mvc.perform(request).andReturn();

        SsnValidatorResponse expectedResponse = new SsnValidatorResponse(false, SsnValidatorController.ERROR_SSN_IS_EMPTY);
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString(), "SsnValidatorController.validateSsnEndpoint() should return false and error that ssn is empty");
    }
}