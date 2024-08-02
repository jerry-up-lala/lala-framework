package com.jerry.up.lala.framework.boot.redis;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * <p>Description: 缓存组件#list
 *
 * @author FMJ
 * @date 2023/8/31 13:23
 */
@Component
public class RedisListTemplate<V> extends RedisBaseTemplate<V> {

    public List<V> get(String key) {
        return get(key, 0, -1);
    }

    public List<V> get(String key, long start, long end) {
        return operations().range(key, start, end);
    }

    public Long addHead(String key, V v) {
        return operations().leftPush(key, v);
    }

    public Long addTail(String key, V v) {
        return operations().rightPush(key, v);
    }

    public void edit(String key, long index, V v) {
        operations().set(key, index, v);
    }

    public void removeHead(String key, long count) {
        for (int i = 0; i < count; i++) {
            operations().leftPop(key);
        }
    }

    public void removeTail(String key, long count) {
        for (int i = 0; i < count; i++) {
            operations().rightPop(key);
        }
    }

    public Long size(String key) {
        return operations().size(key);
    }

    public void addAll(String key, Collection<V> values) {
        operations().rightPushAll(key, values);
    }

    private ListOperations<String, V> operations() {
        return getRedisTemplate().opsForList();
    }
}
