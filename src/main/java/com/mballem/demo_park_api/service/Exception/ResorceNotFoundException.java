package com.mballem.demo_park_api.service.Exception;

public class ResorceNotFoundException extends RuntimeException {
    public ResorceNotFoundException(String message) {
        super(message);
    }
}
