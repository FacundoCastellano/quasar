package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.config.SatelliteConfiguration;
import com.fcastellano.quasar.exception.CommunicationException;
import com.fcastellano.quasar.exception.SatelliteException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.repository.CommunicationRepository;
import com.fcastellano.quasar.service.CommunicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final CommunicationRepository communicationRepository;
    private final SatelliteConfiguration satelliteConfiguration;

    public CommunicationServiceImpl(CommunicationRepository communicationRepository,
                                    SatelliteConfiguration satelliteConfiguration) {
        this.communicationRepository = communicationRepository;
        this.satelliteConfiguration = satelliteConfiguration;
    }

    @Override
    public void addCommunication(String remoteAddr, Communication communication) throws SatelliteException {

        if(!satelliteConfiguration.isValidName(communication.getName()))
            throw new SatelliteException("Name is invalid");

        communicationRepository.add(remoteAddr, communication);
    }

    @Override
    public List<Communication> getCommunications(String remoteAddr) throws CommunicationException {

        if(Boolean.FALSE.equals(communicationRepository.exist(remoteAddr)))
            throw new CommunicationException("IP doesnt exist in cache");

        return communicationRepository.findAll(remoteAddr);
    }

    @Override
    public void clearCommunication(String remoteAddr) {
        communicationRepository.delete(remoteAddr);
    }
}
