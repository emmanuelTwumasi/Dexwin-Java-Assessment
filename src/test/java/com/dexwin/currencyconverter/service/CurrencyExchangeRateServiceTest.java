package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.model.ErrorResponse;
import com.dexwin.currencyconverter.model.CurrencyConversionResponse;
import com.dexwin.currencyconverter.model.ErrorDetail;
import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CurrencyExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CurrencyExchangeRateService currencyExchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvert_Success() throws Exception {
        String jsonResponse = "{\"success\":true,\"result\":1.2}";
        CurrencyConversionResponse response = new CurrencyConversionResponse();
        response.setResult(1.2);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);


        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(jsonNode);
        when(jsonNode.get("success")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("success").asBoolean()).thenReturn(true);
        when(objectMapper.readValue(jsonResponse, CurrencyConversionResponse.class)).thenReturn(response);

        double result = currencyExchangeRateService.convert("USD", "EUR", 100);
        assertEquals(1.2, result);
    }

    @Test
    void testSendLiveRequest_Success() throws Exception {
        String jsonResponse = "{\"success\":true}";
        ExchangeRateResponse response = new ExchangeRateResponse();

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(jsonNode);
        when(jsonNode.get("success")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("success").asBoolean()).thenReturn(true);
        when(objectMapper.readValue(jsonResponse, ExchangeRateResponse.class)).thenReturn(response);

        ExchangeRateResponse result = currencyExchangeRateService.sendLiveRequest();
        assertNotNull(result);
    }

    @Test
    void testConvert_ApiError() throws Exception {
        String jsonResponse = "{\"success\":false,\"error\":{\"info\":\"Invalid API key\"}}";

        ErrorResponse errorResponse = new ErrorResponse();
        ErrorDetail error = new ErrorDetail();
        error.setType("invalid_access_key");
        error.setCode(101);
        error.setInfo("Invalid API key");
        errorResponse.setError(error);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(jsonNode);
        when(jsonNode.get("success")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("success").asBoolean()).thenReturn(false);
        when(objectMapper.readValue(jsonResponse, ErrorResponse.class)).thenReturn(errorResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            currencyExchangeRateService.convert("USD", "EUR", 100);
        });
        assertEquals("Failed to process request: Invalid API key", exception.getMessage());
    }

    @Test
    void testSendLiveRequest_ApiError() throws Exception {
        String jsonResponse = "{\"success\":false,\"error\":{\"info\":\"Invalid API key\"}}";

        ErrorResponse errorResponse = new ErrorResponse();
        ErrorDetail error = new ErrorDetail();
        error.setType("invalid_access_key");
        error.setCode(101);
        error.setInfo("Invalid API key");
        errorResponse.setError(error);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(jsonNode);
        when(jsonNode.get("success")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("success").asBoolean()).thenReturn(false);
        when(objectMapper.readValue(jsonResponse, ErrorResponse.class)).thenReturn(errorResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            currencyExchangeRateService.sendLiveRequest();
        });
        assertEquals("Failed to process request: Invalid API key", exception.getMessage());
    }

}