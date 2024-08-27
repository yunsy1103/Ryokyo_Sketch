package com.travel.japan.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class APIResponse {

    public void printErrorMessage(Exception e) {
        // Implementation of error logging
        System.err.println("Error: " + e.getMessage());
    }

    public ResponseEntity getResponseEntity(Locale locale, String code, Object response) {
        HttpStatus status = code.equals(ResponseCode.CD_SUCCESS) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(response);
    }
}