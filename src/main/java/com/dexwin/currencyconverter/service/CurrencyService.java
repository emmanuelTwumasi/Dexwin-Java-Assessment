package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

public interface CurrencyService {

    double convert(String source, String target, double amount);

    ExchangeRateResponse sendLiveRequest();
}

