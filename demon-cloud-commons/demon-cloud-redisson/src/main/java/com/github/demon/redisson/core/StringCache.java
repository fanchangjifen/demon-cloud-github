package com.github.demon.redisson.core;

public interface StringCache extends Cache {

    void put(String key, String value);

    String get(String key);

}
