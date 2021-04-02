package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.SatelliteException;

public interface SatelliteService {

    void validateExistence(String satelliteName) throws SatelliteException;

}
