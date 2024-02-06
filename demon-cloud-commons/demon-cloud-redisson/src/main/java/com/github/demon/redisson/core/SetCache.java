package com.github.demon.redisson.core;

import java.util.Collection;
import java.util.Set;

public interface SetCache {

    Boolean set(String key, Object table);

    Boolean set(String key, Collection<?> value);

    Boolean zSet(String key, Object value,long timeout);

    Boolean contains(String key,Object value);

    Boolean zContains(String key,Object value);

    Boolean remove(String key,Collection<?> value);

    Set<Object> get(String key);

}
