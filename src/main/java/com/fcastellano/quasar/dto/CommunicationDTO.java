package com.fcastellano.quasar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fcastellano.quasar.model.Communication;

import java.io.Serializable;
import java.util.List;

public class CommunicationDTO implements Serializable {

    private static final long serialVersionUID = -8225744326803988473L;

    @JsonProperty("satellites")
    private List<Communication> communications;

    public List<Communication> getCommunications() {
        return communications;
    }

    public void setCommunications(List<Communication> communications) {
        this.communications = communications;
    }
}
