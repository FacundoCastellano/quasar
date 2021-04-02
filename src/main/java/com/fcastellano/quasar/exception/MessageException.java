package com.fcastellano.quasar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incomplete message")
public class MessageException extends Exception {

    private static final long serialVersionUID = 9180107394264989142L;

    public MessageException(String message) {
        super(message);
    }
}
