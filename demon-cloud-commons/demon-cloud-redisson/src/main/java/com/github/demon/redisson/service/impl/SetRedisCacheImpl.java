package com.github.demon.redisson.service.impl;

import com.github.demon.redisson.service.SetRedisCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class SetRedisCacheImpl implements SetRedisCache {

    private final RedissonClient client;

    public SetRedisCacheImpl(RedissonClient client) {
        this.client = client;
    }

    @Override
    public Boolean set(String key, Object value) {
        return client.getSet(key,StringCodec.INSTANCE).add(value);
    }

    @Override
    public Boolean set(String key, Collection<?> value) {
        return client.getSet(key, StringCodec.INSTANCE).addAll(value);
    }

    @Override
    public Boolean zSet(String key, Object value,long timeout){
        return client.getSetCache(key).add(value,timeout,TimeUnit.SECONDS);
    }

    @Override
    public Boolean contains(String key,Object value) {
        return client.getSet(key).contains(value);
    }

    @Override
    public Boolean zContains(String key,Object value) {
        return client.getSetCache(key).contains(value);
    }

    @Override
    public Boolean remove(String key,Collection<?>  value) {
        return client.getSet(key,StringCodec.INSTANCE).removeAll(value);
    }

    @Override
    public Set<Object> get(String key) {
        return client.getSet(key,StringCodec.INSTANCE).readAll();
    }

    @Override
    public Boolean contains(String key) {
        return client.getSet(key).isExists();
    }

}
