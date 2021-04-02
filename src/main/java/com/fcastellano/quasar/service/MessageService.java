package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.MessageException;

import java.util.List;

public interface MessageService {

    String getMessage(List<String[]> messages) throws MessageException;
}
