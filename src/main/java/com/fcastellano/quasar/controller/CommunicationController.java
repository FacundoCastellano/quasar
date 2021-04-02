package com.fcastellano.quasar.controller;

import com.fcastellano.quasar.dto.CommunicationDTO;
import com.fcastellano.quasar.dto.SpaceShipInfoDTO;
import com.fcastellano.quasar.exception.CommunicationException;
import com.fcastellano.quasar.exception.MessageException;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.service.CommunicationService;
import com.fcastellano.quasar.service.LocationService;
import com.fcastellano.quasar.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommunicationController {

    private final LocationService locationService;
    private final MessageService messageService;
    private final CommunicationService communicationService;

    public CommunicationController(LocationService locationService,
                                   MessageService messageService,
                                   CommunicationService communicationService) {
        this.locationService = locationService;
        this.messageService = messageService;
        this.communicationService = communicationService;
    }

    @PostMapping("/topSecret")
    public ResponseEntity<SpaceShipInfoDTO> topSecret(@RequestBody CommunicationDTO communicationList) throws MessageException {

        List<String[]> messages = mapMessages(communicationList.getSatellites());
        List<Double> distances = mapDistances(communicationList.getSatellites());

        String messageComplete = messageService.getMessage(messages);
        Position position = locationService.getLocation(distances);

        return ResponseEntity.ok(new SpaceShipInfoDTO(position, messageComplete));
    }

    @PostMapping("/topsecret_split/{satellite_name}")
    public ResponseEntity<?> topSecretSplit(@PathVariable("satellite_name") String satelliteName,
                                            @RequestBody Communication communication,
                                            HttpServletRequest httpServletRequest) throws SatelliteException {

        communication.setName(satelliteName);
        communicationService.addCommunication(httpServletRequest.getRemoteAddr(), communication);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/topsecret_split")
    public ResponseEntity<SpaceShipInfoDTO> topSecretSplit(HttpServletRequest httpServletRequest) throws CommunicationException {

        List<Communication> communicationList = communicationService.getCommunications(httpServletRequest.getRemoteAddr());

        List<String[]> messages = mapMessages(communicationList);
        List<Double> distances = mapDistances(communicationList);

        String messageComplete;
        Position position;

        try {
            messageComplete = messageService.getMessage(messages);
            position = locationService.getLocation(distances);
        } catch (Exception e) {
            throw new CommunicationException("Need more info");
        }

        communicationService.clearCommunication(httpServletRequest.getRemoteAddr());

        return ResponseEntity.ok(new SpaceShipInfoDTO(position, messageComplete));
    }

    private List<Double> mapDistances(List<Communication> communicationList) {
        return communicationList.stream()
                .map(Communication::getDistance)
                .collect(Collectors.toList());
    }

    private List<String[]> mapMessages(List<Communication> communicationList) {
        return communicationList.stream()
                .map(Communication::getMessage)
                .collect(Collectors.toList());
    }
}
