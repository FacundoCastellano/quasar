package com.fcastellano.quasar.service;

import com.fcastellano.quasar.exception.CommunicationException;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Communication;

import java.util.List;

public interface CommunicationService {

    void addCommunication(String remoteAddr, Communication communication) throws SatelliteException;

    List<Communication> getCommunications(String remoteAddr) throws CommunicationException;

    void clearCommunication(String remoteAddr);
}
