package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.model.CurrencyConversionResponse;
import com.dexwin.currencyconverter.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * TODO: Implementation of this class has to be backed by https://api.exchangerate.host/latest?base=EUR&symbols=AUD,CAD,CHF,CNY,GBP,JPY,USD
 */

@Service
public class CurrencyExchangeRateService implements CurrencyService {


    public static final String BASE_URL = "http://api.exchangerate.host/";
    public static final String ENDPOINT = "live";

    static CloseableHttpClient httpClient = HttpClients.createDefault();
    @Value("${api.security.access_key.secret}")
    private String access_key;

    @Override
    public double convert(String source, String target, double amount) {
        try {
            String url = UriComponentsBuilder.newInstance().scheme("https").host("api.exchangerate.host").path("/convert").queryParam("access_key", access_key).queryParam("from", source).queryParam("to", target).queryParam("amount", String.valueOf(amount)).build().toUriString();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.sendAsync(HttpRequest.newBuilder().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString()).join();

            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();

            if (response.statusCode() != 200) {
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                throw new RuntimeException("Failed to fetch data from the API");
            } else {
                CurrencyConversionResponse result = objectMapper.readValue(responseBody, CurrencyConversionResponse.class);
                return result.getResult();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch data from the API", e);
        }
    }

    @Override
    public JSONObject sendLiveRequest() {

        try {
            HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + access_key);

            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
            httpClient.close();
            return exchangeRates;
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("Failed to fetch data from the API", e);
        }

    }
}

