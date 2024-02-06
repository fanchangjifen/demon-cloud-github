package com.github.demon.redisson.service.impl;

import com.github.demon.redisson.service.StreamRedisCache;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author Jasmine He
 * @date 2022-06-08
 */
@Component
public class StreamRedisCacheImpl implements StreamRedisCache {

    private final RedissonClient client;

    public StreamRedisCacheImpl(RedissonClient client) {
        this.client = client;
    }

    @Override
    public void addGroup(String stream, String group){
        client.getStream(stream).createGroup(group);
    }

    @Override
    public String add(String stream, String key, Object value) {
        StreamMessageId streamMessageId = client.getStream(stream, StringCodec.INSTANCE).add(StreamAddArgs.entry(key, value));
        return String.valueOf(streamMessageId);
    }

    @Override
    public void readGroup(String stream, String group, String consumer, Integer count) {
        RStream<String, String> rStream = client.getStream(stream);
        Map<StreamMessageId, Map<String, String>> s = rStream.readGroup(group, consumer, count);
        if(s!=null && s.size()>0){
            for (Map.Entry<StreamMessageId, Map<String, String>> entry : s.entrySet()) {
                Map<String, String> m2 = entry.getValue();
                for(Map.Entry<String, String> entry1 : m2.entrySet()){
                    System.out.println(Thread.currentThread().getName()+" : Key = " + entry1.getKey() + ", Value = " + entry1.getValue());
                }
                //消费了消息，要应答一下
                rStream.ack(group, entry.getKey());
                //如果消费了消息想删除，可以删除掉
                rStream.remove(entry.getKey());
            }
        }
    }

    @Override
    public Boolean contains(String stream) {
        return client.getStream(stream).isExists();
    }
}
