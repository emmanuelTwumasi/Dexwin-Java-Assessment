package com.dexwin.currencyconverter.model;

public class CurrencyConversionResponse {

    private String terms;
    private String privacy;
    private Query query;
    private Info info;
    private double result;


    public CurrencyConversionResponse() {
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

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public static class Query {
        private String from;
        private String to;
        private double amount;

        public Query() {
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    public static class Info {
        private long timestamp;
        private double quote;

        public Info() {
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public double getQuote() {
            return quote;
        }

        public void setQuote(double quote) {
            this.quote = quote;
        }


    }

    @Override
    public String toString() {
        return "CurrencyConversionResponse{" +
                ", terms='" + terms + '\'' +
                ", privacy='" + privacy + '\'' +
                ", query=" + query +
                ", info=" + info +
                ", result=" + result +
                '}';
    }
}

