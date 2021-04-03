package com.fcastellano.quasar.service.impl;

import com.fcastellano.quasar.exception.CommunicationException;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.repository.CommunicationRepository;
import com.fcastellano.quasar.service.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    public static final String IP_DOESNT_EXIST_IN_CACHE = "IP doesnt exist in cache";
    private final Logger logger = LoggerFactory.getLogger(CommunicationServiceImpl.class);

    private final CommunicationRepository communicationRepository;

    public CommunicationServiceImpl(CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
    }

    @Override
    public void addCommunication(String remoteAddr, Communication communication) {
        logger.debug("Adding communication. IP: " + remoteAddr);
        communicationRepository.add(remoteAddr, communication);
    }

    @Override
    public List<Communication> getCommunications(String remoteAddr) throws CommunicationException {

        logger.debug("Check communication. IP: " + remoteAddr);
        if(Boolean.FALSE.equals(communicationRepository.exist(remoteAddr))){
            logger.error(IP_DOESNT_EXIST_IN_CACHE + ": " + remoteAddr);
            throw new CommunicationException(IP_DOESNT_EXIST_IN_CACHE);
        }

        logger.debug("Finding communications. IP: " + remoteAddr);
        return communicationRepository.findAll(remoteAddr);
    }

    @Override
    public void clearCommunication(String remoteAddr) {
        logger.debug("Deleting communication. IP: " + remoteAddr);
        communicationRepository.delete(remoteAddr);
    }
}
