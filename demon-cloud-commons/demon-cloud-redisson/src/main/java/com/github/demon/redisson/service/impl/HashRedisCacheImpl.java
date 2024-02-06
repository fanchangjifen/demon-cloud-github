package com.github.demon.redisson.service.impl;

import com.github.demon.redisson.service.HashRedisCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HashRedisCacheImpl implements HashRedisCache {

    private final RedissonClient client;

    public HashRedisCacheImpl(RedissonClient client) {
        this.client = client;
    }

    @Override
    public void put(String key, Map<String, Object> table) {
        client.getMap(key).putAll(table);
    }

    @Override
    public Map<String, Object> get(String key) {
        Map<String, Object> map = new HashMap<>();
        client.getMap(key).readAllEntrySet()
                .stream().forEach(it -> map.put(String.valueOf(it.getKey()), it.getValue()));
        return map;
    }

    @Override
    public Boolean containsHashKey(String key, Object hashKey) {
        return client.getMap(key).containsKey(hashKey);
    }

    @Override
    public Boolean contains(String key) {
        return client.getBucket(key).isExists();
    }

}
