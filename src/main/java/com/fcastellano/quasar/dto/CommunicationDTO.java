package com.fcastellano.quasar.dto;

import com.fcastellano.quasar.model.Communication;

import java.io.Serializable;
import java.util.List;

public class CommunicationDTO implements Serializable {

    private static final long serialVersionUID = -8225744326803988473L;

    private List<Communication> satellites;

    public List<Communication> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<Communication> satellites) {
        this.satellites = satellites;
    }
}
