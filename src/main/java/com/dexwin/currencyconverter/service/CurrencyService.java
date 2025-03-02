package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.model.ExchangeRateResponse;

public interface CurrencyService {

    double convert(String source, String target, double amount);

    ExchangeRateResponse sendLiveRequest();
}

