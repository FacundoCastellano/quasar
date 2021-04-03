package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.CommunicationException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.repository.CommunicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommunicationServiceTest {

    public static final String REMOTE_ADDR = "1.1.1";

    @Autowired
    private CommunicationService communicationService;
    @MockBean
    private CommunicationRepository communicationRepository;
    @Mock
    private Communication communication;
    @Mock
    private List<Communication> communicationList;

    @Test
    public void addCommunication(){
        doNothing().when(communicationRepository).add(anyString(), any(Communication.class));
        communicationService.addCommunication(REMOTE_ADDR, communication);
        verify(communicationRepository).add(REMOTE_ADDR, communication);
    }

    @Test
    public void clearCommunication(){
        doNothing().when(communicationRepository).delete(anyString());
        communicationService.clearCommunication(REMOTE_ADDR);
        verify(communicationRepository).delete(REMOTE_ADDR);
    }

    @Test
    public void getCommunicationsShouldThrowExceptionWhenIpDoesntExist() {
        when(communicationRepository.exist(anyString())).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(CommunicationException.class, () -> communicationService.getCommunications(REMOTE_ADDR));
        verify(communicationRepository).exist(REMOTE_ADDR);
    }

    @Test
    public void getCommunications() throws CommunicationException {
        when(communicationRepository.exist(anyString())).thenReturn(Boolean.TRUE);
        when(communicationRepository.findAll(anyString())).thenReturn(communicationList);
        Assertions.assertEquals(communicationList, communicationService.getCommunications(REMOTE_ADDR));
        verify(communicationRepository).findAll(REMOTE_ADDR);
    }
}
