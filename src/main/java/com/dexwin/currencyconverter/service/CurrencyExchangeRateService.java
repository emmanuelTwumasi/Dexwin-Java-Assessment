package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.model.CurrencyConversionResponse;
import com.dexwin.currencyconverter.model.ErrorResponse;
import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CurrencyExchangeRateService implements CurrencyService {

    private static final Logger LOGGER = Logger.getLogger(CurrencyExchangeRateService.class.getName());
    private static final String BASE_URL = "https://api.exchangerate.host";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${api.security.access_key.secret}")
    private String accessKey;

    public CurrencyExchangeRateService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public double convert(String source, String target, double amount) {
        try {
            // Build the URL dynamically
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .path("/convert")
                    .queryParam("access_key", accessKey)
                    .queryParam("from", source)
                    .queryParam("to", target)
                    .queryParam("amount", amount)
                    .build()
                    .toUriString();

            LOGGER.info("Requesting currency conversion: " + url);

            // Send HTTP Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            LOGGER.info("Response received: Status Code = " + statusCode);

            // Handle Response
            if (statusCode >= 200 && statusCode < 300) {
                return parseCurrencyConversionResponse(responseBody).getResult();
            } else {
                handleError(responseBody, "Failed to convert currency");
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Exception while converting currency", e);
            throw new RuntimeException("Currency conversion failed: ", e);
        }
        return 0; // Unreachable, included for compile-time correctness
    }

    @Override
    public ExchangeRateResponse sendLiveRequest() {
        try {
            // Build the URL dynamically
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .path("/live")
                    .queryParam("access_key", accessKey)
                    .build()
                    .toUriString();

            LOGGER.info("Requesting live exchange rates: " + url);

            // Send HTTP Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            LOGGER.info("Response received: Status Code = " + statusCode);

            // Handle Response
            if (statusCode >= 200 && statusCode < 300) {
                return parseExchangeRateResponse(responseBody);
            } else {
                handleError(responseBody, "Failed to fetch live exchange rates");
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Exception while fetching live exchange rates", e);
            throw new RuntimeException("Live exchange rate request failed: ", e);
        }
        return null;
    }

    // Parses the API response for the `/convert` endpoint
    private CurrencyConversionResponse parseCurrencyConversionResponse(String responseBody) throws IOException {
        return objectMapper.readValue(responseBody, CurrencyConversionResponse.class);
    }

    // Parses the API response for the `/latest` endpoint
    private ExchangeRateResponse parseExchangeRateResponse(String responseBody) throws IOException {
        return objectMapper.readValue(responseBody, ExchangeRateResponse.class);
    }

    // Handles error responses
    private void handleError(String responseBody, String errorMessage) throws IOException {
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        LOGGER.log(Level.SEVERE, errorMessage + ": " + errorResponse);
        throw new RuntimeException(errorMessage + ": " + errorResponse);
    }
}