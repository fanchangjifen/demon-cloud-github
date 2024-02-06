package com.github.demon.redisson.service;

import com.github.demon.redisson.core.Cache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface RedisCache extends Cache {

    @Override
    default Boolean delete(String key) {
        return RedissonClientContext.client.getBucket(key).delete();
    }

    @Override
    default Boolean expire(String key, Duration expire) {
        return RedissonClientContext.client.getBucket(key).expire(expire.getSeconds(), TimeUnit.SECONDS);
    }

    @Component
    class RedissonClientContext {
        private static RedissonClient client;

        public static void setClient(RedissonClient client) {
            RedissonClientContext.client = client;
        }

        public RedissonClientContext(RedissonClient client) {
            setClient(client);
        }
    }

}
