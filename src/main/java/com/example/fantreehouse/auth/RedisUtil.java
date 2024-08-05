package com.example.fantreehouse.auth;

import com.example.fantreehouse.domain.user.entity.MailAuth;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service

public class RedisUtil {
  @Autowired
  private RedisTemplate<String, MailAuth> redisTemplate;//Redis에 접근하기 위한 Spring의 Redis 템플릿 클래스

  public MailAuth getData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
    ValueOperations<String, MailAuth> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void setData(String key, MailAuth value) {//지정된 키(key)에 값을 저장하는 메서드
    ValueOperations<String, MailAuth> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, value);
  }

  public void setDataExpire(String key, MailAuth value, long duration) {//지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
    ValueOperations<String, MailAuth> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(key, value, expireDuration);
  }

  public void deleteData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 삭제하는 메서드
    redisTemplate.delete(key);
  }
}
