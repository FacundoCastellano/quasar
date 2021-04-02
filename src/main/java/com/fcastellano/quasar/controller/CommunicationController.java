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
import com.fcastellano.quasar.service.SatelliteService;
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
    private final SatelliteService satelliteService;

    public CommunicationController(LocationService locationService,
                                   MessageService messageService,
                                   CommunicationService communicationService,
                                   SatelliteService satelliteService) {
        this.locationService = locationService;
        this.messageService = messageService;
        this.communicationService = communicationService;
        this.satelliteService = satelliteService;
    }

    @PostMapping("/topSecret")
    public ResponseEntity<SpaceShipInfoDTO> topSecret(@RequestBody CommunicationDTO communicationList)
            throws MessageException, SatelliteException {

        for (Communication communication : communicationList.getCommunications()) {
            satelliteService.validateExistence(communication.getName());
        }

        String messageComplete = messageService.getMessage(mapMessages(communicationList.getCommunications()));
        Position position = locationService.getLocation(mapDistances(communicationList.getCommunications()));

        return ResponseEntity.ok(new SpaceShipInfoDTO(position, messageComplete));
    }

    @PostMapping("/topsecret_split/{satellite_name}")
    public ResponseEntity<?> topSecretSplit(@PathVariable("satellite_name") String satelliteName,
                                            @RequestBody Communication communication,
                                            HttpServletRequest httpServletRequest) throws SatelliteException {

        satelliteService.validateExistence(satelliteName);

        communication.setName(satelliteName);
        communicationService.addCommunication(httpServletRequest.getRemoteAddr(), communication);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/topsecret_split")
    public ResponseEntity<SpaceShipInfoDTO> topSecretSplit(HttpServletRequest httpServletRequest) throws CommunicationException {

        List<Communication> communicationList = communicationService.getCommunications(httpServletRequest.getRemoteAddr());

        String messageComplete;
        Position position;

        try {
            messageComplete = messageService.getMessage(mapMessages(communicationList));
            position = locationService.getLocation(mapDistances(communicationList));
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
