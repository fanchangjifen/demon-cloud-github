package com.github.demon.redisson.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * redisson 客户端配置
 * @author GilbertPan
 * @version 1.0
 * @date 2022-02-18
 */
@Configuration
@EnableCaching
@ConditionalOnClass(Config.class)
public class RedissonConfig {

    private RedisProperties redisProperties;

    public RedissonConfig(RedisProperties redisProperties){
        this.redisProperties = redisProperties;
    }

    private static final String prefix = "redis://";

    private int connectionMinimumIdleSize = 10;
    private int idleConnectionTimeout = 10000;
    private int pingTimeout = 1000;
    private int connectTimeout = 10000;
    private int timeout = 3000;
    private int retryAttempts = 3;
    private int retryInterval = 1500;
    private int reconnectionTimeout = 3000;
    private int failedAttempts = 3;
    private int subscriptionsPerConnection = 5;
    private String clientName = null;
    private int subscriptionConnectionMinimumIdleSize = 1;
    private int subscriptionConnectionPoolSize = 50;
    private int connectionPoolSize = 640;
    private int database = 0;
    private int dnsMonitoringInterval = 5000;
    private int slaveConnectionPoolSize = 1000;

    private int masterConnectionPoolSize = 500;

    /**
     * 哨兵模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes")
    RedissonClient redissonCluster() {
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers().setScanInterval(2000)
                .addNodeAddress().addNodeAddress(this.getClusterAddresses())
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setMasterConnectionPoolSize(masterConnectionPoolSize)
                .setSlaveConnectionPoolSize(slaveConnectionPoolSize)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setReadMode(ReadMode.SLAVE)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection);

        if (!StringUtils.isEmpty(this.getPassword())) {
            serverConfig.setPassword(this.getPassword());
        }
        config.setCodec(defaultCodec());
        return Redisson.create(config);
    }

    /**
     * 哨兵模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.sentinel.master")
    RedissonClient redissonSentinel() {
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(this.getSentinelAddresses())
                .setMasterName(this.getMasterName())
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setMasterConnectionPoolSize(masterConnectionPoolSize)
                .setSlaveConnectionPoolSize(slaveConnectionPoolSize)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setReadMode(ReadMode.SLAVE)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection);

        if (!StringUtils.isEmpty(this.getPassword())) {
            serverConfig.setPassword(this.getPassword());
        }
        config.setCodec(defaultCodec());
        return Redisson.create(config);
    }

    /**
     * 单机模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.host")
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress(this.getAddress())
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setDatabase(database)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPassword(this.getPassword());
        config.setCodec(defaultCodec());
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    public String getAddress() {
        return prefix + redisProperties.getHost() + ":" + redisProperties.getPort();
    }

    public String[] getSentinelAddresses() {
        List<String> nodesObject = redisProperties.getSentinel().getNodes();
        List<String> nodes = new ArrayList<>(nodesObject.size());
        nodesObject.stream().forEach(node -> {
            if (!node.startsWith(prefix)) {
                nodes.add(prefix + node);
            } else {
                nodes.add(node);
            }
        });
        return nodes.toArray(new String[nodes.size()]);
    }

    public String getMasterName() {
        return redisProperties.getSentinel().getMaster();
    }

    public String[] getClusterAddresses() {
        List<String> nodesObject = redisProperties.getCluster().getNodes();
        List<String> nodes = new ArrayList<>(nodesObject.size());
        nodesObject.stream().forEach(node -> {
            if (!node.startsWith(prefix)) {
                nodes.add(prefix + node);
            } else {
                nodes.add(node);
            }
        });
        return nodes.toArray(new String[nodes.size()]);
    }

    public String getPassword() {
        return redisProperties.getPassword();
    }

    public Codec defaultCodec() {
        return StringCodec.INSTANCE;
    }

}
