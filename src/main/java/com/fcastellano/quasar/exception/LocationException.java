package com.fcastellano.quasar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incomplete location")
public class LocationException extends Exception {

    public LocationException(String message) {
        super(message);
    }
}
