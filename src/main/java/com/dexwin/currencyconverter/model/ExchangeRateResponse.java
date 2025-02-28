package com.dexwin.currencyconverter.model;

import java.util.Map;

public class ExchangeRateResponse {
    public ExchangeRateResponse() {
    }

    private boolean success;
    private String terms;
    private String privacy;
    private long timestamp;
    private String source;
    private Map<String, Double> quotes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Double> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, Double> quotes) {
        this.quotes = quotes;
    }

    @Override
    public String toString() {
        return "CurrencyConversionResponse{" +
                "success=" + success +
                ", terms='" + terms + '\'' +
                ", privacy='" + privacy + '\'' +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                ", quotes=" + quotes +
                '}';
    }
}
