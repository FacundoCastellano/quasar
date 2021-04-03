package com.fcastellano.quasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcastellano.quasar.dto.CommunicationDTO;
import com.fcastellano.quasar.exception.LocationException;
import com.fcastellano.quasar.exception.MessageException;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.service.CommunicationService;
import com.fcastellano.quasar.service.LocationService;
import com.fcastellano.quasar.service.MessageService;
import com.fcastellano.quasar.service.SatelliteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = CommunicationController.class)
@WebMvcTest(CommunicationController.class)
public class CommunicationControllerTest {

    public static final String INVALID_NAME = "INVALID_NAME";
    public static final String MESSAGE = "MESSAGE";
    public static final double POSITION_X = 1.0;
    public static final double POSITION_Y = 2.0;
    public static final String VALID_NAME = "Kenobi";
    public static final String TOPSECRET_ENDPOINT = "/topsecret";
    public static final String TOPSECRET_SPLIT_ENDPOINT = "/topsecret_split/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommunicationService communicationService;
    @MockBean
    private LocationService locationService;
    @MockBean
    private MessageService messageService;
    @MockBean
    private SatelliteService satelliteService;

    private Communication communication;
    private CommunicationDTO communicationDTO;

    @BeforeEach
    public void setUp(){
        communication = new Communication();
        communicationDTO = new CommunicationDTO();
    }

    @Test
    void topSecretWithInvalidSatelliteShouldThrowException() throws Exception {
        communication.setName(INVALID_NAME);
        communicationDTO.setCommunications(Collections.singletonList(communication));
        doThrow(SatelliteException.class).when(satelliteService).getPosition(anyString());
        performTopSecretPostNotFound(communicationDTO);
        verify(satelliteService).getPosition(INVALID_NAME);
    }

    @Test
    void topSecretWithInvalidMessageShouldThrowException() throws Exception {
        communicationDTO.setCommunications(Collections.singletonList(communication));
        when(satelliteService.getPosition(any())).thenReturn(new Position());
        doThrow(MessageException.class).when(messageService).getMessage(any());
        performTopSecretPostNotFound(communicationDTO);
        verify(messageService).getMessage(any());
    }

    @Test
    void topSecretWithInvalidLocationShouldThrowException() throws Exception {
        communicationDTO.setCommunications(Collections.singletonList(communication));
        when(satelliteService.getPosition(any())).thenReturn(new Position());
        when(messageService.getMessage(any())).thenReturn(MESSAGE);
        doThrow(LocationException.class).when(locationService).getLocation(any(), any());
        performTopSecretPostNotFound(communicationDTO);
        verify(locationService).getLocation(any(), any());
    }

    @Test
    void topSecret() throws Exception {
        Position correctPosition = new Position(POSITION_X, POSITION_Y);
        communicationDTO.setCommunications(Collections.singletonList(communication));
        when(satelliteService.getPosition(any())).thenReturn(new Position());
        when(messageService.getMessage(any())).thenReturn(MESSAGE);
        when(locationService.getLocation(any(),any())).thenReturn(correctPosition);

        mockMvc.perform(post(TOPSECRET_ENDPOINT)
                .content(objectMapper.writeValueAsString(communicationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x").value(POSITION_X))
                .andExpect(jsonPath("$.position.y").value(POSITION_Y))
                .andExpect(jsonPath("$.message").value(MESSAGE));
    }

    @Test
    void topSecretSplitPostWithInvalidSatelliteShouldThrowException() throws Exception {
        doThrow(SatelliteException.class).when(satelliteService).validateExistence(anyString());

        mockMvc.perform(post(TOPSECRET_SPLIT_ENDPOINT + INVALID_NAME)
                .content(objectMapper.writeValueAsString(communication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(satelliteService).validateExistence(INVALID_NAME);
    }

    @Test
    void topSecretSplitPost() throws Exception{
        doNothing().when(satelliteService).validateExistence(anyString());
        doNothing().when(communicationService).addCommunication(anyString(), any());

        mockMvc.perform(post(TOPSECRET_SPLIT_ENDPOINT + VALID_NAME)
                .content(objectMapper.writeValueAsString(communication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(communicationService).addCommunication(anyString(), any());
    }

    @Test
    void topSecretSplitGetWithInvalidMessageShouldThrowException() throws Exception {
        when(communicationService.getCommunications(anyString())).thenReturn(Collections.singletonList(communication));
        when(satelliteService.getPosition(any())).thenReturn(new Position());
        doThrow(MessageException.class).when(messageService).getMessage(any());
        performTopSecretSplitGetNotFound(communication);
        verify(communicationService).getCommunications(anyString());
        verify(satelliteService).getPosition(any());
        verify(messageService).getMessage(any());
    }

    @Test
    void topSecretSplitGetWithInvalidLocationShouldThrowException() throws Exception {
        when(communicationService.getCommunications(anyString())).thenReturn(Collections.singletonList(communication));
        when(satelliteService.getPosition(any())).thenReturn(new Position());
        when(messageService.getMessage(any())).thenReturn(MESSAGE);
        doThrow(LocationException.class).when(locationService).getLocation(any(), any());
        performTopSecretSplitGetNotFound(communication);
        verify(locationService).getLocation(any(), any());
    }

    @Test
    void topSecretSplitGet() throws Exception {
        Position correctPosition = new Position(POSITION_X, POSITION_Y);
        when(communicationService.getCommunications(anyString())).thenReturn(Collections.singletonList(communication));
        when(satelliteService.getPosition(any())).thenReturn(new Position());
        when(messageService.getMessage(any())).thenReturn(MESSAGE);
        when(locationService.getLocation(any(),any())).thenReturn(correctPosition);

        mockMvc.perform(get(TOPSECRET_SPLIT_ENDPOINT)
                .content(objectMapper.writeValueAsString(communication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x").value(POSITION_X))
                .andExpect(jsonPath("$.position.y").value(POSITION_Y))
                .andExpect(jsonPath("$.message").value(MESSAGE));
    }

    private void performTopSecretPostNotFound(CommunicationDTO communicationDTO) throws Exception {
        mockMvc.perform(post(TOPSECRET_ENDPOINT)
                .content(objectMapper.writeValueAsString(communicationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private void performTopSecretSplitGetNotFound(Communication communication) throws Exception {
        mockMvc.perform(get(TOPSECRET_SPLIT_ENDPOINT)
                .content(objectMapper.writeValueAsString(communication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
