package com.dexwin.currencyconverter.controller;

import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.dexwin.currencyconverter.service.CurrencyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/currencies")
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(final CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public double convert(
            @RequestParam("source") String source,
            @RequestParam("target") String target,
            @RequestParam("amount") double amount) {
        return currencyService.convert(source, target, amount);
    }

    @GetMapping(value = "/rates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeRateResponse getRates() {
        return currencyService.sendLiveRequest();
    }

}
