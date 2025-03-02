package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.exception.CurrencyConverterException;
import com.dexwin.currencyconverter.model.ErrorResponse;
import com.dexwin.currencyconverter.model.CurrencyConversionResponse;
import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class CurrencyExchangeRateService implements CurrencyService {

    private static final Logger LOGGER = Logger.getLogger(CurrencyExchangeRateService.class.getName());
    private static final String BASE_URL = "https://api.exchangerate.host";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.security.access_key.secret}")
    private String accessKey;

    public CurrencyExchangeRateService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public double convert(String source, String target, double amount) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL).path("/convert")
                .queryParam("access_key", accessKey)
                .queryParam("from", source)
                .queryParam("to", target)
                .queryParam("amount", String.valueOf(amount));

        String url = builder.build().toUriString();
        LOGGER.info("Requesting currency conversion: " + url);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return handleResponse(responseEntity, CurrencyConversionResponse.class).getResult();
        } catch (IOException e) {
            LOGGER.severe("Deserialization error: " + e.getMessage());
            throw new RuntimeException("Failed to deserialize the response: ", e);
        }
    }

    @Override
    public ExchangeRateResponse sendLiveRequest() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL).path("/live")
                .queryParam("access_key", accessKey);

        String url = builder.build().toUriString();
        LOGGER.info("Requesting live exchange rates: " + url);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return handleResponse(responseEntity, ExchangeRateResponse.class);
        } catch (IOException e) {
            LOGGER.severe("Deserialization error: " + e.getMessage());
            throw new RuntimeException("Failed to deserialize the response: ", e);
        }
    }

    private <T> T handleResponse(ResponseEntity<String> responseEntity, Class<T> responseType) throws IOException {
        String responseBody = responseEntity.getBody();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.get("success").asBoolean()) {
                LOGGER.info("Successfully converted currency exchange rates: " + responseBody);
                return objectMapper.readValue(responseBody, responseType);
            } else {
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                LOGGER.severe("API error: " + errorResponse.getError().getInfo());
                throw new CurrencyConverterException(errorResponse);
            }
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
            LOGGER.severe("API error: " + errorResponse.getError().getInfo());
            throw new RuntimeException("Failed to process request: " + errorResponse.getError().getInfo());
        }
    }

}