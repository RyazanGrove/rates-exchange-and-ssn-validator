package com.ryazangrove.ratesexchangeandssnvalidator.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SsnValidatorControllerUnitTest {

    @Test
    void checkSsnForErrorsNullAndEmptyValue() {
        assertEquals(SsnValidatorController.ERROR_SSN_IS_EMPTY, SsnValidatorController.checkSsnForErrors(null), "SsnValidatorController.checkSsnForErrors() should handle null ssn");
        assertEquals(SsnValidatorController.ERROR_SSN_IS_EMPTY, SsnValidatorController.checkSsnForErrors(""), "SsnValidatorController.checkSsnForErrors() should handle empty ssn");
    }

    @Test
    void checkSsnForErrorsIncorrectFormat() {
        assertEquals(SsnValidatorController.ERROR_SSN_IS_IN_INCORRECT_FORMAT, SsnValidatorController.checkSsnForErrors("27A258A448E"), "SsnValidatorController.checkSsnForErrors() should handle incorrect format of ssn");
        assertEquals(SsnValidatorController.ERROR_SSN_IS_IN_INCORRECT_FORMAT, SsnValidatorController.checkSsnForErrors("270258a448E"), "SsnValidatorController.checkSsnForErrors() should handle incorrect format of ssn");
        assertEquals(SsnValidatorController.ERROR_SSN_IS_IN_INCORRECT_FORMAT, SsnValidatorController.checkSsnForErrors("270258A448e"), "SsnValidatorController.checkSsnForErrors() should handle incorrect format of ssn");
        assertEquals(SsnValidatorController.ERROR_SSN_IS_IN_INCORRECT_FORMAT, SsnValidatorController.checkSsnForErrors("270258A44EE"), "SsnValidatorController.checkSsnForErrors() should handle incorrect format of ssn");

    }

    @Test
    void checkSsnForErrorsCorrectValue() {
        assertEquals(SsnValidatorController.CORRECT, SsnValidatorController.checkSsnForErrors("270258A448E"), "SsnValidatorController.checkSsnForErrors() should handle correct ssn");
        assertEquals(SsnValidatorController.CORRECT, SsnValidatorController.checkSsnForErrors("131052-308T"), "SsnValidatorController.checkSsnForErrors() should handle correct ssn");
    }

    @Test
    void checkCountryCodeForErrorsNullAndEmptyValue() {
        assertEquals(SsnValidatorController.ERROR_COUNTRY_CODE_IS_EMPTY, SsnValidatorController.checkCountryCodeForErrors(null), "SsnValidatorController.checkCountryCodeForErrors() should handle null country code");
        assertEquals(SsnValidatorController.ERROR_COUNTRY_CODE_IS_EMPTY, SsnValidatorController.checkCountryCodeForErrors(""), "SsnValidatorController.checkCountryCodeForErrors() should handle empty country code");
    }

    @Test
    void checkCountryCodeForErrorsCheckForSupportedCountryCodes() {
        assertEquals(SsnValidatorController.ERROR_COUNTRY_CODE_IS_NOT_SUPPORTED, SsnValidatorController.checkCountryCodeForErrors("SE"), "SsnValidatorController.checkCountryCodeForErrors() should handle not supported country codes");
        assertEquals(SsnValidatorController.ERROR_COUNTRY_CODE_IS_NOT_SUPPORTED, SsnValidatorController.checkCountryCodeForErrors("DE"), "SsnValidatorController.checkCountryCodeForErrors() should handle not supported country codes");
    }

    @Test
    void checkCountryCodeForErrorsCorrectValue() {
        assertEquals(SsnValidatorController.CORRECT, SsnValidatorController.checkCountryCodeForErrors(SsnValidatorController.COUNTRY_CODE_FINLAND), "SsnValidatorController.checkCountryCodeForErrors() should handle supported country code");
    }

    @Test
    void validateSsnRandomString() {
        assertEquals(false, SsnValidatorController.validateSsn("absfj65487"), "SsnValidatorController.validateSsn() handles random string");
    }

    @Test
    void validateSsnCheckDates() {
        assertEquals(false, SsnValidatorController.validateSsn("290222A456P"), "SsnValidatorController.validateSsn() handles not existing dates");
        assertEquals(false, SsnValidatorController.validateSsn("350390-1236"), "SsnValidatorController.validateSsn() handles not existing dates");
        assertEquals(false, SsnValidatorController.validateSsn("181785+008H"), "SsnValidatorController.validateSsn() handles not existing dates");
    }

    @Test
    void validateSsnIdNumberInBoundaries() {
        // Boundaries are specified here: https://dvv.fi/en/personal-identity-code
        assertEquals(false, SsnValidatorController.validateSsn("230222A0015"), "SsnValidatorController.validateSsn() handles not id number boundary range");
        assertEquals(false, SsnValidatorController.validateSsn("030390-907U"), "SsnValidatorController.validateSsn() handles not id number boundary range");
    }

    @Test
    void validateSsnControlCharacter() {
        assertEquals(false, SsnValidatorController.validateSsn("230222A1235"), "SsnValidatorController.validateSsn() handles incorrect control character");
        assertEquals(false, SsnValidatorController.validateSsn("030390-709U"), "SsnValidatorController.validateSsn() handles incorrect control character");
    }

    @Test
    void validateSsnCorrectValue() {
        assertEquals(true, SsnValidatorController.validateSsn("230222A1233"), "SsnValidatorController.validateSsn() handles correct value");
        assertEquals(true, SsnValidatorController.validateSsn("030390-709E"), "SsnValidatorController.validateSsn() handles correct value");
    }
}