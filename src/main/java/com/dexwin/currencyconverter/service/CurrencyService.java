package com.dexwin.currencyconverter.service;

import org.json.JSONObject;

public interface CurrencyService {

    double convert(String source, String target, double amount);

    // sendLiveRequest() function is created to request and retrieve the data
    JSONObject sendLiveRequest();
}
