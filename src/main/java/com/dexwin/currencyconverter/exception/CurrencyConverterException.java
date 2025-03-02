package com.dexwin.currencyconverter.exception;

import com.dexwin.currencyconverter.model.ErrorResponse;

public class CurrencyConverterException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public CurrencyConverterException(ErrorResponse errorResponse) {
        super(errorResponse.getError().getInfo());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
