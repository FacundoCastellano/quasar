package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.config.SatelliteConfiguration;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.model.Satellite;
import com.fcastellano.quasar.service.SatelliteService;
import org.springframework.stereotype.Service;

@Service
public class SatelliteServiceImpl implements SatelliteService {

    private final SatelliteConfiguration satelliteConfiguration;

    public SatelliteServiceImpl(SatelliteConfiguration satelliteConfiguration) {
        this.satelliteConfiguration = satelliteConfiguration;
    }

    @Override
    public void validateExistence(String satelliteName) throws SatelliteException {
        satelliteConfiguration.getSatellites().stream()
                .filter(satellite -> satellite.getName().equalsIgnoreCase(satelliteName))
                .findFirst()
                .orElseThrow(() ->new SatelliteException("satellite not found"));
    }

    @Override
    public Position getPosition(String name) throws SatelliteException {
        return satelliteConfiguration.getSatellites().stream()
                .filter(satellite -> satellite.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(Satellite::getPosition)
                .orElseThrow(() ->new SatelliteException("satellite not found"));
    }
}
