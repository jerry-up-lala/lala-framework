package com.jerry.up.lala.framework.core.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 缓存组件#hash
 *
 * @author FMJ
 * @date 2023/8/31 13:23
 */
@Component
public class RedisHashTemplate<HK, HV> extends RedisBaseTemplate<HV> {

    public Map<HK, HV> getAll(String key) {
        return operations().entries(key);
    }

    public HV get(String key, HK hk) {
        return operations().get(key, hk);
    }

    public void put(String key, HK hk, HV hv) {
        operations().put(key, hk, hv);
    }

    public void putAll(String key, Map<HK, HV> map) {
        operations().putAll(key, map);
    }

    public void putAll(String key, Map<HK, HV> map, long timeout) {
        putAll(key, map, timeout, TimeUnit.SECONDS);
    }

    public void putAll(String key, Map<HK, HV> map, long timeout, TimeUnit timeUnit) {
        putAll(key, map);
        setExpire(key, timeout, timeUnit);
    }

    public void remove(String key, Object... hashKeys) {
        operations().delete(key, hashKeys);
    }

    public boolean hasKey(String key, HK hk) {
        return operations().hasKey(key, hk);
    }

    private HashOperations<String, HK, HV> operations() {
        return getRedisTemplate().opsForHash();
    }
}
