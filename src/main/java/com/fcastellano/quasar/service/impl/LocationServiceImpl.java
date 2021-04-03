package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.exception.LocationException;
import com.fcastellano.quasar.model.Position;
import com.fcastellano.quasar.service.LocationService;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    public static final String NEED_MORE_DATA_TO_GET_LOCATION = "Need more data to get location";
    private final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

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

            logger.debug("Getting location");
            centroid = solver.solve().getPoint().toArray();
        } catch (Exception e) {
            logger.error(NEED_MORE_DATA_TO_GET_LOCATION +
                    ". Distances size: " + distances.size() +
                    " Positions size: " + positions.size());
            throw new LocationException(NEED_MORE_DATA_TO_GET_LOCATION);
        }

        return new Position(centroid[0], centroid[1]);
    }
}
