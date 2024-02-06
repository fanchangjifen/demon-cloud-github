package com.github.demon.redisson.service.impl;

import com.github.demon.redisson.service.StringRedisCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
class StringRedisCacheImpl implements StringRedisCache {

    private final RedissonClient client;

    StringRedisCacheImpl(RedissonClient client) {
        this.client = client;
    }

    @Override
    public void put(String key, String value) {
        client.getBucket(key, StringCodec.INSTANCE).set(value);
    }

    @Override
    public String get(String key) {
        Object value = client.getBucket(key,StringCodec.INSTANCE).get();
        return Objects.isNull(value)?"":value.toString();
    }

    @Override
    public void put(String key, String value, Duration expire) {
        client.getBucket(key).set(value, expire.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Boolean putIfNotPresent(String key, String value, Duration expire) {
        return client.getBucket(key).trySet(value, expire.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Boolean putIfNotPresent(String key, String value) {
        return client.getBucket(key).trySet(value);
    }

    @Override
    public void putIfPresent(String key, String value) {
        client.getBucket(key).set(value);
    }

    @Override
    public Boolean contains(String key) {
        return !StringUtils.isEmpty(get(key));
    }

}
