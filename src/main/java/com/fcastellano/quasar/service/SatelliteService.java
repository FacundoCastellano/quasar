package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Position;

public interface SatelliteService {

    void validateExistence(String satelliteName) throws SatelliteException;

    Position getPosition(String name) throws SatelliteException;
}
