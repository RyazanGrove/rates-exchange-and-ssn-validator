package com.ryazangrove.ratesexchangeandssnvalidator.controllers;

import com.ryazangrove.ratesexchangeandssnvalidator.models.SnnRequestBody;
import com.ryazangrove.ratesexchangeandssnvalidator.models.SsnValidatorResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SsnValidatorController {

    public static final String CORRECT = "OK";
    public static final String ERROR_SSN_IS_EMPTY = "error: ssn is empty";
    public static final String ERROR_SSN_IS_IN_INCORRECT_FORMAT = "error: snn is in incorrect format";
    public static final String ERROR_COUNTRY_CODE_IS_EMPTY = "error: country code is empty";
    public static final String ERROR_COUNTRY_CODE_IS_NOT_SUPPORTED = "error: country code is not supported";

    public static String COUNTRY_CODE_FINLAND = "FI";
    private static final Map<Integer, String> characterCodes = new HashMap<Integer, String>() {{
        put(0, "0");
        put(1, "1");
        put(2, "2");
        put(3, "3");
        put(4, "4");
        put(5, "5");
        put(6, "6");
        put(7, "7");
        put(8, "8");
        put(9, "9");
        put(10, "A");
        put(11, "B");
        put(12, "C");
        put(13, "D");
        put(14, "E");
        put(15, "F");
        put(16, "H");
        put(17, "J");
        put(18, "K");
        put(19, "L");
        put(20, "M");
        put(21, "N");
        put(22, "P");
        put(23, "R");
        put(24, "S");
        put(25, "T");
        put(26, "U");
        put(27, "V");
        put(28, "W");
        put(29, "X");
        put(30, "Y");

    }};

    @PostMapping("/validate_ssn")
    public SsnValidatorResponse validateSsnEndpoint(@RequestBody SnnRequestBody body){
        String errorMessageSsn = checkSsnForErrors(body.getSsn());
        if(!errorMessageSsn.equals(CORRECT)){
            SsnValidatorResponse ssnValidatorResponse = new SsnValidatorResponse(false, errorMessageSsn);
            return ssnValidatorResponse;
        }
        String errorMessageCountryCode = checkCountryCodeForErrors(body.getCountry_code());
        if(!errorMessageCountryCode.equals(CORRECT)){
            SsnValidatorResponse ssnValidatorResponse = new SsnValidatorResponse(false, errorMessageCountryCode);
            return ssnValidatorResponse;
        }

        SsnValidatorResponse ssnValidatorResponse = new SsnValidatorResponse(validateSsn(body.getSsn()));
        return ssnValidatorResponse;
    }

    public static String checkSsnForErrors(String ssn){
        if(ssn == null || ssn.isEmpty()){
            return ERROR_SSN_IS_EMPTY;
        }
        if(!ssn.matches("\\d{6}[-+A]\\d{3}[A-Y0-9]")) {
            return ERROR_SSN_IS_IN_INCORRECT_FORMAT;
        }
        return CORRECT;
    }

    public static String checkCountryCodeForErrors(String countryCode){
        if(countryCode == null || countryCode.isEmpty()){
            return ERROR_COUNTRY_CODE_IS_EMPTY;
        } else if(!countryCode.equals(COUNTRY_CODE_FINLAND)){
            return ERROR_COUNTRY_CODE_IS_NOT_SUPPORTED;
        }
        return CORRECT;
    }

    public static boolean validateSsn(String ssn){
        int dateOfBirth;
        int dayOfBirth;
        int monthOfBirth;
        int yearOfBirth;
        String centurySign;
        int idNumber;
        String controlCharacter;

        try {
            dateOfBirth = Integer.parseInt(ssn.substring(0, 6));
            dayOfBirth = Integer.parseInt(ssn.substring(0, 2));
            monthOfBirth = Integer.parseInt(ssn.substring(2, 4));
            yearOfBirth = Integer.parseInt(ssn.substring(4, 6));
            centurySign = ssn.substring(6, 7);
            idNumber = Integer.parseInt(ssn.substring(7, 10));
            controlCharacter = ssn.substring(10, 11);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }

        if(centurySign.equals("+")) {
            yearOfBirth += 1800;
        } else if (centurySign.equals("-")) {
            yearOfBirth += 1900;
        } else {
            yearOfBirth += 2000;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearOfBirth);
        cal.set(Calendar.MONTH, monthOfBirth - 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfBirth);
        if(cal.get(Calendar.DAY_OF_MONTH) != dayOfBirth || cal.get(Calendar.MONTH) != monthOfBirth - 1 || cal.get(Calendar.YEAR) != yearOfBirth){
            return false;
        }

        if(idNumber < 2 || idNumber > 899) {
            return false;
        }

        BigDecimal dividedNumber = new BigDecimal(dateOfBirth * 1000 + idNumber).divide(new BigDecimal(31), 20, RoundingMode.HALF_UP);
        BigDecimal remainingPart = dividedNumber.subtract(new BigDecimal(dividedNumber.toBigInteger()));
        int characterCode = remainingPart.multiply(new BigDecimal(31)).setScale(0, RoundingMode.HALF_UP).intValue();

        if(characterCodes.get(characterCode).equals(controlCharacter)) {
            return true;
        } else {
            return false;
        }
    }
}
