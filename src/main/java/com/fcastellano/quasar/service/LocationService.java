package com.fcastellano.quasar.service;

import com.fcastellano.quasar.model.Position;

import java.util.List;

public interface LocationService {

    Position getLocation(List<Double> distances);
}
