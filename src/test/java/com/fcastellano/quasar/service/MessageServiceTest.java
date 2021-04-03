package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.MessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MessageServiceTest {

    public static final String EXPECTED_MSG = "este es un mensaje secreto";
    public static final String EXPECTED_MSG_WITH_DISPLACE_WORD = "este es un mensaje desfasado";

    private List<String[]> messages;

    @Autowired
    private MessageService messageService;

    @BeforeEach
    public void setUp(){
        messages = new ArrayList<>();
    }

    @Test
    public void getMessageWithOneSatellite() throws MessageException {
        messages.add(new String[]{"este", "es", "un", "mensaje", "secreto"});
        assertEquals(messageService.getMessage(messages), EXPECTED_MSG);
    }

    @Test
    public void getMessageWithMultipleSatellites() throws MessageException {
        messages.add(new String[]{"", "es", "", "mensaje", ""});
        messages.add(new String[]{"este", "", "un", "", ""});
        messages.add(new String[]{"", "", "", "", "secreto"});
        assertEquals(messageService.getMessage(messages), EXPECTED_MSG);
    }

    @Test
    public void getMessageWithEmptyListShouldThrowException() {
        assertThrows(MessageException.class, () -> messageService.getMessage(messages));
    }

    @Test
    public void getMessageWithMissingWordShouldThrowException() {
        messages.add(new String[]{"", "", "", "mensaje", ""});
        messages.add(new String[]{"este", "", "un", "", ""});
        messages.add(new String[]{"", "", "", "", "secreto"});
        assertThrows(MessageException.class, () -> messageService.getMessage(messages));
    }

    @Test
    public void getMessageWithDisplacedWord() throws MessageException {
        messages.add(new String[]{"", "este", "es", "un", "mensaje", "desfasado"});
        messages.add(new String[]{"este", "", "un", "mensaje", "desfasado"});
        messages.add(new String[]{"", "es", "", "mensaje", "desfasado"});
        assertEquals(messageService.getMessage(messages), EXPECTED_MSG_WITH_DISPLACE_WORD);
    }

}
