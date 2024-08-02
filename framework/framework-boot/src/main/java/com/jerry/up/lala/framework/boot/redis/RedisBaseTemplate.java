package com.jerry.up.lala.framework.boot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 缓存组件
 *
 * @author FMJ
 * @date 2023/8/31 10:10
 */
@Component
public class RedisBaseTemplate<V> implements RedisBase<V> {

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public RedisTemplate<String, V> getRedisTemplate() {
        return redisTemplate;
    }
}
