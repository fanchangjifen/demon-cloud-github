package com.github.demon.redisson.service.impl;

import com.github.demon.redisson.service.ListRedisCache;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class ListRedisCacheImpl implements ListRedisCache {

    private final RedissonClient client;

    public ListRedisCacheImpl(RedissonClient client) {
        this.client = client;
    }

    @Override
    public Boolean contains(String key) {
        return client.getDeque(key).isExists();
    }

    @Override
    public int add(String key, Object value) {
        return rpush(key, value);
    }

    @Override
    public int addAll(String key, Object... values) {
        return rpush(key, values);
    }

    @Override
    public int addAll(String key, Collection<Object> values) {
        return rpush(key, values);
    }

    @Override
    public Object first(String key) {
        return client.getDeque(key).peekFirst();
    }

    @Override
    public Object last(String key) {
        return client.getDeque(key).peekLast();
    }

    @Override
    public Boolean contains(String key, Object item) {
        return client.getDeque(key).contains(item);
    }

    @Override
    public Integer size(String key) {
        return client.getDeque(key).size();
    }

    @Override
    public int lpush(String key, Object... objects) {
        return lpush(key, Arrays.asList(objects));
    }

    @Override
    public int lpush(String key, Collection<Object> objects) {
        RBatch batch = client.createBatch(BatchOptions.defaults().executionMode(BatchOptions.ExecutionMode.IN_MEMORY_ATOMIC));
        objects.forEach(it -> batch.getDeque(key).addFirstAsync(it));
        return batch.execute().getSyncedSlaves();
    }

    @Override
    public int lpush(String key, Collection<Object> objects, boolean originOrder) {
        Object[] os = objects.toArray(new Object[0]);
        List<Object> newCollection = new ArrayList<>(objects.size());
        for (int i = objects.size() - 1; i >= 0; i--) {
            newCollection.add(os[i]);
        }
        return lpush(key, newCollection);
    }

    @Override
    public int rpush(String key, Object... objects) {
        return rpush(key, Arrays.asList(objects));
    }

    @Override
    public int rpush(String key, Collection<Object> objects) {
        RBatch batch = client.createBatch(BatchOptions.defaults().executionMode(BatchOptions.ExecutionMode.IN_MEMORY_ATOMIC));
        objects.forEach(it -> batch.getDeque(key).addLastAsync(it));
        return batch.execute().getSyncedSlaves();
    }

    @Override
    public List<Object> readAll(String key){
       return client.getQueue(key).readAll();
    }
}
