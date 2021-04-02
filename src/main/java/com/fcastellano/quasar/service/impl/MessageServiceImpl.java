package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.exception.MessageException;
import com.fcastellano.quasar.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public String getMessage(List<String[]> messages) throws MessageException {

        String[] messageReduced = messages.stream()
                .sorted(Comparator.comparingInt(strings -> strings.length))
                .reduce(this::completeMessage)
                .orElse(new String[]{""});

        if (Arrays.asList(messageReduced).contains("")) throw new MessageException("mensaje incompleto");

        return String.join(" ", messageReduced);
    }

    private String[] completeMessage(String[] messageToComplete, String[] message) {
        IntStream
                .range(0, messageToComplete.length)
                .filter(index -> "".equals(messageToComplete[index]))
                .forEach(index -> {
                    int indexMessage = messageToComplete.length == message.length? index : index + 1;
                    messageToComplete[index] = message[indexMessage];
                });
        return messageToComplete;
    }

}