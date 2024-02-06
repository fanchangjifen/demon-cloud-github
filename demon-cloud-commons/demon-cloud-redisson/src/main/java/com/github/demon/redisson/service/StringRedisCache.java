package com.github.demon.redisson.service;

import com.github.demon.redisson.core.StringCache;

import java.time.Duration;

public interface StringRedisCache extends RedisCache, StringCache {

    /**
     * set with expire
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     * @return true if set
     */
    void put(String key, String value, Duration expire);

    /**
     * set if not present with expire
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     * @return true if set
     */
    Boolean putIfNotPresent(String key, String value, Duration expire);

    /**
     * set if key not exist
     *
     * @param key   key
     * @param value value
     * @return true if set
     */
    Boolean putIfNotPresent(String key, String value);

    /**
     * set if key exist
     *
     * @param key   key
     * @param value value
     * @return true if set
     */
    void putIfPresent(String key, String value);

}
