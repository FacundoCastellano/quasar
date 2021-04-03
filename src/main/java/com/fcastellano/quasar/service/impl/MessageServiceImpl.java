package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.exception.MessageException;
import com.fcastellano.quasar.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MessageServiceImpl implements MessageService {

    public static final String INCOMPLETE_MESSAGE = "incomplete message";
    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public String getMessage(List<String[]> messages) throws MessageException {

        logger.debug("Completing message");
        String[] messageReduced = messages.stream()
                .sorted(Comparator.comparingInt(strings -> strings.length))
                .reduce(this::completeMessage)
                .orElse(new String[]{""});

        if (Arrays.asList(messageReduced).contains("")) {
            logger.error(INCOMPLETE_MESSAGE + ". Message: " + Arrays.toString(messageReduced));
            throw new MessageException(INCOMPLETE_MESSAGE);
        }

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