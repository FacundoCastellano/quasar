package com.fcastellano.quasar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid satellite")
public class SatelliteException extends Exception {

    private static final long serialVersionUID = -4813348201054825114L;

    public SatelliteException(String message) {
        super(message);
    }
}
