package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.LocationException;
import com.fcastellano.quasar.model.Position;

import java.util.List;

public interface LocationService {

    Position getLocation(List<Double> distances, List<Position> positions) throws LocationException;
}
