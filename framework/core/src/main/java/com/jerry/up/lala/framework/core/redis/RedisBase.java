package com.jerry.up.lala.framework.core.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 缓存组件
 *
 * @author FMJ
 * @date 2023/8/31 10:10
 */
public interface RedisBase<V> {

    default Long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    default Long getExpire(String key, TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    default Boolean persist(String key) {
        return getRedisTemplate().persist(key);
    }

    default Boolean setExpire(String key, long timeout) {
        return setExpire(key, timeout, TimeUnit.SECONDS);
    }

    default Boolean setExpire(String key, long timeout, TimeUnit timeUnit) {
        return getRedisTemplate().expire(key, timeout, timeUnit);
    }

    default Set<String> keys(String pattern) {
        return getRedisTemplate().keys(pattern);
    }

    default DataType dataType(String key) {
        return getRedisTemplate().type(key);
    }

    default Boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    default void delete(String... keys) {
        Arrays.stream(keys).forEach(key -> getRedisTemplate().delete(key));
    }

    RedisTemplate<String, V> getRedisTemplate();
}
