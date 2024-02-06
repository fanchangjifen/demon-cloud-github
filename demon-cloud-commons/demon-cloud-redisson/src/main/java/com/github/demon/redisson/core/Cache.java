package com.github.demon.redisson.core;

import java.time.Duration;

public interface Cache {

    String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * delete key
     *
     * @param key key
     * @return true if deleted
     */
    Boolean delete(String key);

    /**
     * set expire time
     *
     * @param key    key
     * @param expire expire time
     * @return true if set
     */
    Boolean expire(String key, Duration expire);

    /**
     * Whether key exist or not.
     *
     * @param key key
     * @return true if key exist
     */
    Boolean contains(String key);

}
