package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.exception.LocationException;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.service.LocationService;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Override
    public Position getLocation(List<Double> distances, List<Position> positions) throws LocationException {

        double[][] positionsArray = positions.stream()
                .map(Position::getArray)
                .toArray(double[][]::new);
        double[] distancesArray = distances.stream().mapToDouble(value -> value).toArray();

        double[] centroid;
        try {
            TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positionsArray, distancesArray);
            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());

            centroid = solver.solve().getPoint().toArray();
        } catch (Exception e) {
            throw new LocationException("Need more data to get location");
        }

        return new Position(centroid[0], centroid[1]);
    }
}
