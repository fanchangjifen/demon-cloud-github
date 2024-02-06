package com.github.demon.redisson.core;

import java.util.Map;

public interface HashCache extends Cache {

    void put(String key, Map<String, Object> table);

    Map<String, Object> get(String key);

    Boolean containsHashKey(String key, Object hashKey);

    Boolean contains(String key);
}
