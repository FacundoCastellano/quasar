package com.fcastellano.quasar.repository;

import com.fcastellano.quasar.model.Communication;

import java.util.List;

public interface CommunicationRepository {

    void add(String remoteAddr, Communication communication);

    Boolean exist(String remoteAddr);

    List<Communication> findAll(String remoteAddr);

    void delete(String remoteAddr);
}
