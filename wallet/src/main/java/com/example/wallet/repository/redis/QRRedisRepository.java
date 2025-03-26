package com.example.wallet.repository.redis;

import com.example.wallet.dto.QRCodeData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class QRRedisRepository {

    private final RedisTemplate<String, QRCodeData> redisTemplate;

    public void save(String key, QRCodeData value, long t) {
        redisTemplate.opsForValue().set(key, value, t, TimeUnit.SECONDS);
    }

    public QRCodeData find(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
