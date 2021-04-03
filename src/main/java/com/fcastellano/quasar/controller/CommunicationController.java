package com.fcastellano.quasar.controller;

import com.fcastellano.quasar.dto.CommunicationDTO;
import com.fcastellano.quasar.dto.InfoCommunications;
import com.fcastellano.quasar.dto.SpaceShipInfoDTO;
import com.fcastellano.quasar.exception.CommunicationException;
import com.fcastellano.quasar.exception.LocationException;
import com.fcastellano.quasar.exception.MessageException;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.service.CommunicationService;
import com.fcastellano.quasar.service.LocationService;
import com.fcastellano.quasar.service.MessageService;
import com.fcastellano.quasar.service.SatelliteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommunicationController {

    private final Logger logger = LoggerFactory.getLogger(CommunicationController.class);

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

    @PostMapping("/topsecret")
    public ResponseEntity<SpaceShipInfoDTO> topSecret(@RequestBody CommunicationDTO communicationList)
            throws MessageException, SatelliteException, LocationException {

        logger.info("Begin Top Secret");
        InfoCommunications infoMapped = mapInfoCommunications(communicationList.getCommunications());

        String messageComplete = messageService.getMessage(infoMapped.getMessages());
        Position position = locationService.getLocation(infoMapped.getDistances(), infoMapped.getPositions());

        logger.info("Ending Top Secret Successfully");
        return ResponseEntity.ok(new SpaceShipInfoDTO(position, messageComplete));
    }

    @PostMapping("/topsecret_split/{satellite_name}")
    public ResponseEntity<?> topSecretSplit(@PathVariable("satellite_name") String satelliteName,
                                            @RequestBody Communication communication,
                                            HttpServletRequest httpServletRequest) throws SatelliteException {

        logger.info("Begin Top Secret Split Post");
        satelliteService.validateExistence(satelliteName);

        communication.setName(satelliteName);
        communicationService.addCommunication(httpServletRequest.getRemoteAddr(), communication);

        logger.info("Ending Top Secret Split Post Successfully");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/topsecret_split")
    public ResponseEntity<SpaceShipInfoDTO> topSecretSplit(HttpServletRequest httpServletRequest) throws CommunicationException {

        logger.info("Begin Top Secret Split Get");
        List<Communication> communicationList = communicationService.getCommunications(httpServletRequest.getRemoteAddr());

        String messageComplete;
        Position position;

        try {
            InfoCommunications infoMapped = mapInfoCommunications(communicationList);
            messageComplete = messageService.getMessage(infoMapped.getMessages());
            position = locationService.getLocation(infoMapped.getDistances(), infoMapped.getPositions());
        } catch (Exception e) {
            logger.info("Ending Top Secret Split Get with error", e);
            throw new CommunicationException("Need more info");
        }

        communicationService.clearCommunication(httpServletRequest.getRemoteAddr());

        logger.info("Ending Top Secret Split Get Successfully");
        return ResponseEntity.ok(new SpaceShipInfoDTO(position, messageComplete));
    }

    private InfoCommunications mapInfoCommunications(List<Communication> communicationList) throws SatelliteException {
        logger.debug("Mapping info");
        InfoCommunications infoMapped = new InfoCommunications();
        for (Communication communication : communicationList) {
            infoMapped.getPositions().add(satelliteService.getPosition(communication.getName()));
            infoMapped.getDistances().add(communication.getDistance());
            infoMapped.getMessages().add(communication.getMessage());
        }
        return infoMapped;
    }
}
