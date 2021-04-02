package com.fcastellano.quasar.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcastellano.quasar.model.Communication;
import com.fcastellano.quasar.repository.CommunicationRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class CacheCommunicationRepository implements CommunicationRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, HashMap<String, Communication>> redisTemplate;

    public CacheCommunicationRepository(ObjectMapper objectMapper,
                                        RedisTemplate<String, HashMap<String, Communication>> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void add(String remoteAddr, Communication communication) {
        redisTemplate.opsForHash().put(remoteAddr, communication.getName(), communication);
    }

    @Override
    public Boolean exist(String remoteAddr) {
        return redisTemplate.hasKey(remoteAddr);
    }

    @Override
    public List<Communication> findAll(String remoteAddr) {
        return  objectMapper
                .convertValue(redisTemplate.opsForHash().values(remoteAddr),
                        new TypeReference<ArrayList<Communication>>() {});
    }

    @Override
    public void delete(String remoteAddr) {
        redisTemplate.delete(remoteAddr);
    }
}
