package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.config.SatelliteConfiguration;
import com.fcastellano.quasar.exception.SatelliteException;
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
        if(!satelliteConfiguration.isValidName(satelliteName))
            throw new SatelliteException("Name is invalid");
    }
}
