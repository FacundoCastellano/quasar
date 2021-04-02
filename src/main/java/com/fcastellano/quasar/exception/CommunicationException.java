package com.fcastellano.quasar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Need more information")
public class CommunicationException extends Exception{

    private static final long serialVersionUID = 8755375041135795816L;

    public CommunicationException(String message) {
        super(message);
    }
}
