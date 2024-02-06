package com.github.demon.redisson.service;

import com.github.demon.redisson.core.ListCache;

import java.util.Collection;
import java.util.List;

public interface ListRedisCache extends ListCache, RedisCache {

    int lpush(String key, Object... objects);

    int lpush(String key, Collection<Object> objects);

    int lpush(String key, Collection<Object> objects, boolean originOrder);

    int rpush(String key, Object... objects);

    int rpush(String key, Collection<Object> objects);

    List<Object> readAll(String key);
}
