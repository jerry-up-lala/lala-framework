package com.jerry.up.lala.framework.boot.redis;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p>Description: 缓存组件#value
 *
 * @author FMJ
 * @date 2023/8/31 10:10
 */
@Component
public class RedisZSetTemplate<V> extends RedisBaseTemplate<V> {

    public Set<V> getAll(String key) {
        return get(key, 0, -1);
    }

    public Set<V> get(String key, long start, long end) {
        return operations().range(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<V>> getWithScoresAll(String key) {
        return getWithScores(key, 0, -1);
    }

    public Set<ZSetOperations.TypedTuple<V>> getWithScores(String key, long start, long end) {
        return operations().rangeWithScores(key, start, end);
    }

    public Double getScore(String key, V v) {
        return operations().score(key, v);
    }

    public Set<V> getByScore(String key, double min, double max) {
        return operations().rangeByScore(key, min, max);
    }

    public Boolean add(String key, V v, double score) {
        return operations().add(key, v, score);
    }

    public Long add(String key, Set<ZSetOperations.TypedTuple<V>> tuples) {
        return operations().add(key, tuples);
    }

    public Long remove(String key, Object... values) {
        return operations().remove(key, values);
    }

    private ZSetOperations<String, V> operations() {
        return getRedisTemplate().opsForZSet();
    }
}
