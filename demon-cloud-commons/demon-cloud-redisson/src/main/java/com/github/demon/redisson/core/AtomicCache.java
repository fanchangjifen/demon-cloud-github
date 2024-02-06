package com.github.demon.redisson.core;

public interface AtomicCache extends Cache {

    Long get(String key);

    void set(String key, Long value);

    Boolean setExpire(String key, Long seconds);

    Long incrementAndGet(String key);

    Long decrementAndGet(String key);
}
