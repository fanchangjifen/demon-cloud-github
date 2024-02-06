package com.github.demon.redisson.core;

import java.util.Collection;
import java.util.List;

public interface ListCache {

    int add(String key, Object value);

    int addAll(String key, Object... values);

    int addAll(String key, Collection<Object> values);

    Object first(String key);

    Object last(String key);

    Boolean contains(String key, Object item);

    Integer size(String key);

    List<Object> readAll(String key);

}
