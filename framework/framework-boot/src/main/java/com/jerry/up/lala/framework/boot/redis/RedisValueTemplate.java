package com.jerry.up.lala.framework.boot.redis;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 缓存组件#value
 *
 * @author FMJ
 * @date 2023/8/31 10:10
 */
@Component
public class RedisValueTemplate<V> extends RedisBaseTemplate<V> {

    public V get(String key) {
        return operations().get(key);
    }

    public List<V> multiGet(List<String> keys) {
        return operations().multiGet(keys);
    }

    public void set(String key, V value) {
        operations().set(key, value);
    }

    public void set(String key, V value, long timeout) {
        operations().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(String key, V value, long timeout, TimeUnit unit) {
        operations().set(key, value, timeout, unit);
    }

    public Long incr(String key) {
        return operations().increment(key);
    }

    public Long incr(String key, long delta) {
        return operations().increment(key, delta);
    }

    public Long decr(String key) {
        return operations().decrement(key);
    }

    public Long decr(String key, long delta) {
        return operations().decrement(key, delta);
    }

    private ValueOperations<String, V> operations() {
        return getRedisTemplate().opsForValue();
    }
}
