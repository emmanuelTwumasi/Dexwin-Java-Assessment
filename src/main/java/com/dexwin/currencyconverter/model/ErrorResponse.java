package com.dexwin.currencyconverter.model;

public class ErrorResponse {
    private boolean success;
    private ErrorDetail error;

    public ErrorResponse() {
    }

    public ErrorResponse(Exception e) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode(500);
        errorDetail.setType("Internal Server ErrorDetail");
        errorDetail.setInfo(e.getMessage());
        this.setError(errorDetail);
        this.setSuccess(Boolean.FALSE);
    }


    public ErrorDetail getError() {
        return error;
    }

    public void setError(ErrorDetail errorResponse) {
        this.error = errorResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error=" + error +
                '}';
    }
}