package com.example.fantreehouse.common.config;

import com.example.fantreehouse.domain.user.entity.MailAuth;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.port}")
  private int port;

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.password}")
  private String setPassword;

  // TCP 통신
  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
    redisStandaloneConfiguration.setPassword(setPassword);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);

  }

  @Bean
  public RedisTemplate<String, MailAuth> redisTemplate() {
    RedisTemplate<String, MailAuth> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());   // Key: String
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<MailAuth>(MailAuth.class));  // Value: 직렬화에 사용할 Object 사용하기
    return redisTemplate;
  }


}