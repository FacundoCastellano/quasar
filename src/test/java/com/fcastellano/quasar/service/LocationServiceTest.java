package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.LocationException;
import com.fcastellano.quasar.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LocationServiceTest {

    public static final double DELTA = 0.1;
    @Autowired
    private LocationService locationService;

    private static Stream<Arguments> getLocationExceptionProvider() {
        return Stream.of(
                Arguments.arguments(Collections.EMPTY_LIST, Collections.EMPTY_LIST),
                Arguments.arguments(Collections.EMPTY_LIST, Collections.singletonList(new Position())),
                Arguments.arguments(Collections.EMPTY_LIST, Arrays.asList(new Position(), new Position())),
                Arguments.arguments(Collections.singletonList(2.0), Arrays.asList(new Position(), new Position()))
        );
    }

    private static Stream<Arguments> getLocationProvider() {
        return Stream.of(
                Arguments.arguments(Arrays.asList(59.05, 182.31, 63.01),
                        Arrays.asList(new Position(5, 3), new Position(123.23, -32), new Position(8, 45)),
                        new Position(-50.8, 22.34)),
                Arguments.arguments(Arrays.asList(1.0, 1.0, 2.82),
                        Arrays.asList(new Position(1, 0), new Position(0, 1), new Position(-1, -1)),
                        new Position(1, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("getLocationExceptionProvider")
    void getLocationWithLessInformationShouldThrowException(List<Double> distances, List<Position> positions) {
        assertThrows(LocationException.class, () -> locationService.getLocation(distances, positions));
    }

    @ParameterizedTest
    @MethodSource("getLocationProvider")
    void getLocation(List<Double> distances, List<Position> positions, Position result) throws LocationException {
        Position position = locationService.getLocation(distances,positions);
        assertEquals(result.getX(), position.getX(), DELTA);
        assertEquals(result.getY(), position.getY(), DELTA);
    }
}
