package com.jerry.up.lala.framework.boot.redis;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p>Description: 缓存组件#value
 *
 * @author FMJ
 * @date 2023/8/31 10:10
 */
@Component
public class RedisSetTemplate<V> extends RedisBaseTemplate<V> {

    public Set<V> get(String key) {
        return operations().members(key);
    }

    public Long add(String key, V... values) {
        return operations().add(key, values);
    }

    public Long remove(String key, Object... values) {
        return operations().remove(key, values);
    }

    private SetOperations<String, V> operations() {
        return getRedisTemplate().opsForSet();
    }
}
