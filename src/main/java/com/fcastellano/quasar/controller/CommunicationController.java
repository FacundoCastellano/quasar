package com.fcastellano.quasar.controller;

import com.fcastellano.quasar.dto.CommunicationDTO;
import com.fcastellano.quasar.dto.SpaceShipInfoDTO;
import com.fcastellano.quasar.exception.MessageException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.service.LocationService;
import com.fcastellano.quasar.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommunicationController {

    private final LocationService locationService;
    private final MessageService messageService;

    public CommunicationController(LocationService locationService,
                                   MessageService messageService) {
        this.locationService = locationService;
        this.messageService = messageService;
    }

    @PostMapping("/topSecret")
    public ResponseEntity<SpaceShipInfoDTO> topSecret(@RequestBody CommunicationDTO communicationList) throws MessageException {

        List<String[]> messages = mapMessages(communicationList.getSatellites());
        List<Double> distances = mapDistances(communicationList.getSatellites());

        String messageComplete = messageService.getMessage(messages);
        Position position = locationService.getLocation(distances);

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
