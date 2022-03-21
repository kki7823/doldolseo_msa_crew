package com.doldolseo.doldolseo_msa_crew.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<Long, String> redisTemplate;

    public void put(Long key, String value, Long expTimeMin) {
        redisTemplate.opsForValue().set(key, value, expTimeMin, TimeUnit.MINUTES);
    }

    public void put(Long key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(Long key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(Long key) {
        redisTemplate.delete(key);
    }

    public boolean isExist(Long key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
