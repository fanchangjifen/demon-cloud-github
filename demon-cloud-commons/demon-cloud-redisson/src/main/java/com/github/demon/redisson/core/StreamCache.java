package com.github.demon.redisson.core;

/**
 * <p>
 *
 * </p>
 *
 * @author Jasmine He
 * @date 2022-06-08
 */
public interface StreamCache {

    /**
     * 创建消费组
     * @param stream
     * @param group
     */
    void addGroup(String stream, String group);

    /**
     * 消费消息
     * @param stream
     * @param group
     * @param consumer
     * @param count
     * @return
     */
    void readGroup(String stream, String group, String consumer, Integer count);

    /**
     * 新增消息
     * @param stream
     * @param key
     * @param value
     * @return
     */
    String add(String stream, String key, Object value);

}
