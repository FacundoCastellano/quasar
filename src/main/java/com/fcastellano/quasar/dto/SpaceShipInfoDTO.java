package com.fcastellano.quasar.dto;

import com.fcastellano.quasar.model.Position;

import java.io.Serializable;

public class SpaceShipInfoDTO implements Serializable {

    private static final long serialVersionUID = -6716116650947554904L;

    private Position position;
    private String message;

    public SpaceShipInfoDTO(Position position, String message) {
        this.position = position;
        this.message = message;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
