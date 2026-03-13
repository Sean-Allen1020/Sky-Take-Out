package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("Generate redis template instance");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置Redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置Redis key序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

//        // JSON 序列化（推荐）
//        StringRedisSerializer keySerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(keySerializer);
//        redisTemplate.setHashKeySerializer(keySerializer);
//
//        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
//        redisTemplate.setValueSerializer(valueSerializer);
//        redisTemplate.setHashValueSerializer(valueSerializer);
//
//        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
