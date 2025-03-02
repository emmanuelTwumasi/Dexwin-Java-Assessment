package com.dexwin.currencyconverter.controller;

import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.dexwin.currencyconverter.service.CurrencyService;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void should_convert_EUR_to_USD_with_rate_greater_than_1() throws Exception {
        when(currencyService.convert(anyString(), anyString(), eq(1.0)))
                .thenReturn(1.2);
        this.mockMvc.perform(get("/api/v1/currencies/convert?source=EUR&target=USD&amount=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new AssertionMatcher<>() {
                            @Override
                            public void assertion(String value) throws AssertionError {
                                assertThat(parseDouble(value)).isGreaterThan(1.0);
                            }
                        })
                );
    }

    @Test
    public void should_convert_USD_to_EUR_with_rate_less_than_1() throws Exception {
        when(currencyService.convert(anyString(), anyString(), eq(1.0)))
                .thenReturn(0.8);

        this.mockMvc.perform(get("/api/v1/currencies/convert?source=USD&target=EUR&amount=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new AssertionMatcher<>() {
                            @Override
                            public void assertion(String value) throws AssertionError {
                                assertThat(parseDouble(value)).isLessThan(1.0);
                            }
                        })
                );
    }

    @Test
    void testGetRates() throws Exception {
        ExchangeRateResponse response = getExchangeRateResponse();

        when(currencyService.sendLiveRequest()).thenReturn(response);

        this.mockMvc.perform(get("/api/v1/currencies/rates"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(
                        new AssertionMatcher<>() {
                            @Override
                            public void assertion(String value) throws AssertionError {
                                assertThat(JsonPath.parse(value).read("$.success", Boolean.class)).isTrue();
                                assertThat(JsonPath.parse(value).read("$.quotes", Map.class)).isEqualTo(response.getQuotes());
                                assertThat(JsonPath.parse(value).read("$.source", String.class)).isEqualTo(response.getSource());
                                assertThat(JsonPath.parse(value).read("$.terms", String.class)).isEqualTo(response.getTerms());
                                assertThat(JsonPath.parse(value).read("$.privacy", String.class)).isEqualTo(response.getPrivacy());
                                assertThat(JsonPath.parse(value).read("$.timestamp", Long.class)).isEqualTo(response.getTimestamp());
                            }
                        })
                );
    }

    private static ExchangeRateResponse getExchangeRateResponse() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setSuccess(true);
        response.setTerms("https://currencylayer.com/terms");
        response.setPrivacy("https://currencylayer.com/privacy");
        response.setTimestamp(1740926711L);
        response.setSource("USD");
        Map<String, Double> quotes = new HashMap<>();
        quotes.put("USDAED", 3.672704);
        quotes.put("USDAFN", 73.751144);
        quotes.put("USDALL", 95.524833);
        quotes.put("USDAMD", 393.756925);
        quotes.put("USDANG", 1.805578);
        quotes.put("USDAOA", 915.503981);
        quotes.put("USDARS", 1064.054531);
        quotes.put("USDAUD", 1.610565);
        quotes.put("USDAWG", 1.8);
        quotes.put("USDAZN", 1.70397);
        quotes.put("USDBAM", 1.884291);
        quotes.put("USDBBD", 2.022833);
        quotes.put("USDBDT", 121.720699);
        quotes.put("USDBGN", 1.884291);
        quotes.put("USDBHD", 0.376704);
        quotes.put("USDBIF", 2967.483983);
        quotes.put("USDBMD", 1.0);
        quotes.put("USDBND", 1.350932);
        quotes.put("USDBOB", 6.922299);
        response.setQuotes(quotes);
        return response;
    }
}