package com.yk.mqcenter.config;

import java.time.Duration;

import com.yk.mqcenter.util.RedisUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.database:0}")
    protected Integer database;
    @Value("${spring.redis.host}")
    protected String host;
    @Value("${spring.redis.port}")
    protected Integer port;
    @Value("${spring.redis.password:}")
    protected String password;
    @Value("${spring.redis.lettuce.pool.max-active}")
    protected Integer maxActive;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    protected Integer maxWait;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    protected Integer maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    protected Integer minIdle;
    @Value("${spring.redis.lettuce.shutdown-timeout}")
    protected Integer timeout;

    @Bean
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }

    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        return getTemplate(redisConnectionFactory());
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    protected RedisConnectionFactory redisConnectionFactory() {
        return connectionFactory(maxActive, maxIdle, minIdle, maxWait, host, password, timeout, port, database);
    }

    protected RedisConnectionFactory connectionFactory(Integer maxActive,
                                                       Integer maxIdle,
                                                       Integer minIdle,
                                                       Integer maxWait,
                                                       String host,
                                                       String password,
                                                       Integer timeout,
                                                       Integer port,
                                                       Integer database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        if (StringUtils.isNoneBlank(password)) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        LettuceClientConfiguration lettucePoolingConfig =
                LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).shutdownTimeout(Duration.ofMillis(timeout)).build();
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettucePoolingConfig);
        connectionFactory.afterPropertiesSet();

        return connectionFactory;
    }

    private RedisTemplate<String, Object> getTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = stringRedisSerializer();

        // key string序列化
        template.setKeySerializer(stringRedisSerializer);
        // value jackson序列化
        template.setValueSerializer(jackson2JsonRedisSerializer());
        // hash field string序列化
        template.setHashKeySerializer(stringRedisSerializer);
        // hash value jackson序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }

    protected StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    protected RedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 反序列化时忽略多余字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    //@Bean
    //public RedissonClient redissonClient() {
    //    Config config = new Config();
    //    String redisHost = "redis://" + host + ":" + port;
    //    if (StringUtils.isBlank(password)) {
    //        config.useSingleServer().setAddress(redisHost).setDatabase(database);
    //    } else {
    //        config.useSingleServer().setAddress(redisHost).setPassword(password).setDatabase(database);
    //    }
    //    return Redisson.create(config);
    //}

}
