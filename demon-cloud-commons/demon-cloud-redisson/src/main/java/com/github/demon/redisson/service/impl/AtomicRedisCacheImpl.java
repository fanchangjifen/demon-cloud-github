package com.github.demon.redisson.service.impl;

import com.github.demon.redisson.service.AtomicRedisCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AtomicRedisCacheImpl implements AtomicRedisCache {

    private final RedissonClient client;

    public AtomicRedisCacheImpl(RedissonClient client) {
        this.client = client;
    }

    @Override
    public Long get(String key) {
        return client.getAtomicLong(key).get();
    }

    @Override
    public void set(String key, Long value) {
        client.getAtomicLong(key).set(value);
    }

    @Override
    public Boolean setExpire(String key, Long seconds) {
        return client.getAtomicLong(key).expire(seconds, TimeUnit.SECONDS);
    }

    @Override
    public Long incrementAndGet(String key) {
        return client.getAtomicLong(key).incrementAndGet();
    }

    @Override
    public Long decrementAndGet(String key) {
        return client.getAtomicLong(key).decrementAndGet();
    }

    @Override
    public Boolean contains(String key) {
        return client.getAtomicLong(key).isExists();
    }
}
