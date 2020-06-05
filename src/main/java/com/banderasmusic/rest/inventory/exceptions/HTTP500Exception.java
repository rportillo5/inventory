package com.banderasmusic.ecommerceproductapi.exceptions;

public class HTTP500Exception extends RuntimeException {
    public HTTP500Exception() {
        super();
    }

    public HTTP500Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTP500Exception(String message) {
        super(message);
    }

    public HTTP500Exception(Throwable cause) {
        super(cause);
    }
}
