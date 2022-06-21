# Rates exchange and SSN validator

### Table of Contents
* [How to use](#how-to-use)
* [REST API](#rest-api)
* [Tests](#tests)
* [Requirements](#requirements)


 Application has two REST API endpoints: `validate_ssn` and `exchange_amount`

Endpoint `validate_ssn` designed to validate SSN (Social-security-number) based on the requirements. Requirements depend on the country
issued the number. Only Finland is supported and the requiremets for ssn could be found here [Personal identity code requirements](https://dvv.fi/en/personal-identity-code)

Endpoint `exchange_amount` designed to get exchange rates for different currencies depending on the amount. The rates for the currencies are taken from [APILayer](https://apilayer.com/marketplace/exchangerates_data-api)
Curently only EUR, SEK and USD are supported. To use this endpoint `API_KEY` is required.

Application requests the latest data about exchange rates from APILayer during initialization and then update it hourly (at 0 second 0 minute of every hour) with a `CronTrigger`.

# How to use
Application runs at `localhost:8080`.

### Using IDE
To open the project open `pom.xml` file using Maven.

The application could be started using command at the root of the project:

     java -jar target/rates-exchange-and-ssn-validator-0.0.1-SNAPSHOT.jar YOUR_API_KEY

Where `YOUR_API_KEY` is api key for your account.

### Using Docker
Application could be run via Docker:

    docker pull ryazangrove/rates-exchange-and-ssn-validator:v0.0.1
    docker run -e API_KEY=YOUR_API_KEY -p 8080:8080 rates-exchange-and-ssn-validator

Where `YOUR_API_KEY` is api key for your account.

# REST API

The REST API to the app is described below.

## Validate SSN

### Request

`POST /validate_ssn/`

### Parameters
Parameters are in json body of the request

<b>ssn</b>: (string) social-security-number to verify

<b>country_code</b>: (string) the requirements how ssn would be verified based on the country

### Example

    curl --request POST --url http://localhost:8080/validate_ssn/ -H "Content-Type: application/json" -d '{"ssn": "230222A1233", "country_code": "FI"}' 

### Response

    {
        "ssn_valid": true
    }

### Error cases
In case of error the parameter `error_Message` would be added in response

    {
        "ssn_valid": false,
        "error_Message": "error: country code is empty"
    }

## Exchange Rates

### Request

`GET /exchange_amount/`

### Parameters

<b>from</b>: (string) currency code, from which currency the exchange amount would be calculated

<b>to</b>: (string) currency code, to which currency the exchange amount would be calculated

<b>from_amount</b>: (number) the amount for "from" currency which will be used for calculation

### Example

    curl --request GET --url 'http://localhost:8080/exchange_amount?from=EUR&to=USD&from_amount=300'

### Response

    {
        "from": "EUR",
        "to": "USD",
        "to_amount": 317.44680000000005,
        "exchange_rate": 1.058156
    }

### Error cases
In case of error the parameter `error_Message` would be in response

    {
        "errorMessage": "error: currency is not supported"
    }

# Tests
Tests for application are separated for unit tests and integration tests.
To run all the tests use command:
    
    mvn test

### Unit Tests
Unit tests are defined in classes:

    RatesExchangeControllerUnitTest.java
    RatesExchangeServiceUnitTest.java
    SsnValidatorControllerUnitTest.java
    

### Integration Test
Integration tests are defined in classes:

    RatesExchangeControllerIntTest.java
    SsnValidatorControllerIntTest.java

# Requirements
Java version "16.0.1"

Docker Desktop 4.6.0 (API version: 1.41)
