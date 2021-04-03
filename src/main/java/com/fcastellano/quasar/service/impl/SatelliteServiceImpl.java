package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.config.SatelliteConfiguration;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.model.Satellite;
import com.fcastellano.quasar.service.SatelliteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SatelliteServiceImpl implements SatelliteService {

    public static final String SATELLITE_NOT_FOUND = "satellite not found";
    private final Logger logger = LoggerFactory.getLogger(SatelliteServiceImpl.class);
    private final SatelliteConfiguration satelliteConfiguration;

    public SatelliteServiceImpl(SatelliteConfiguration satelliteConfiguration) {
        this.satelliteConfiguration = satelliteConfiguration;
    }

    @Override
    public void validateExistence(String satelliteName) throws SatelliteException {
        logger.debug("Validating existence of satellite");
        satelliteConfiguration.getSatellites().stream()
                .filter(satellite -> satellite.getName().equalsIgnoreCase(satelliteName))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error(SATELLITE_NOT_FOUND + ". Satellite: " + satelliteName);
                    return new SatelliteException(SATELLITE_NOT_FOUND);
                });
    }

    @Override
    public Position getPosition(String name) throws SatelliteException {
        return satelliteConfiguration.getSatellites().stream()
                .filter(satellite -> satellite.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(Satellite::getPosition)
                .orElseThrow(() -> {
                    logger.error(SATELLITE_NOT_FOUND + ". Satellite: " + name);
                    return new SatelliteException(SATELLITE_NOT_FOUND);
                });
    }
}
