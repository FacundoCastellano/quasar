package com.fcastellano.quasar.dto;

import com.fcastellano.quasar.model.Position;

import java.util.ArrayList;
import java.util.List;

public class InfoCommunications {

    private List<Position> positions;
    private List<Double> distances;
    private List<String[]> messages;

    public InfoCommunications() {
        this.positions = new ArrayList<>();
        this.distances = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<Double> getDistances() {
        return distances;
    }

    public void setDistances(List<Double> distances) {
        this.distances = distances;
    }

    public List<String[]> getMessages() {
        return messages;
    }

    public void setMessages(List<String[]> messages) {
        this.messages = messages;
    }
}
