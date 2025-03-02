package com.dexwin.currencyconverter.exception;

import com.dexwin.currencyconverter.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e);
    }

    @ExceptionHandler(CurrencyConverterException.class)
    public ErrorResponse handleCurrencyConverterException(CurrencyConverterException e) {
        return e.getErrorResponse();
    }
}
