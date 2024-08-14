package com.feilu.api.common.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.pool.maxTotal}")
    private int maxTotal;

    @Value("${redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${redis.pool.minIdle}")
    private int minIdle;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        return new JedisPool(poolConfig, redisHost, redisPort);
    }
}
