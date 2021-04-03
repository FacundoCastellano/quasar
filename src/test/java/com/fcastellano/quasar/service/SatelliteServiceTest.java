package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SatelliteServiceTest {

    public static final String SATELLITE = "Kenobi";
    public static final String INVALID_SATELLITE = "INVALID_SATELLITE";
    public static final double POSITION_X = -500;
    public static final double POSITION_Y = -20;

    @Autowired
    private SatelliteService satelliteService;

    @Test
    public void validateExistenceWithCorrectSatellite() {
        assertDoesNotThrow(() -> satelliteService.validateExistence(SATELLITE));
    }

    @Test
    public void validateExistenceWithInvalidSatelliteShouldThrowException() {
        assertThrows(SatelliteException.class, () -> satelliteService.validateExistence(INVALID_SATELLITE));
    }

    @Test
    public void getPositionWithCorrectSatellite() throws SatelliteException {
        Position position = satelliteService.getPosition(SATELLITE);
        assertEquals(POSITION_X, position.getX());
        assertEquals(POSITION_Y, position.getY());
    }

    @Test
    public void getPositionWithInvalidSatelliteShouldThrowException() {
        assertThrows(SatelliteException.class, () -> satelliteService.getPosition(INVALID_SATELLITE));
    }
}
