package com.mballem.demo_park_api.service.exception;

public class ResorceNotFoundException extends RuntimeException {
    public ResorceNotFoundException(String message) {
        super(message);
    }
}
