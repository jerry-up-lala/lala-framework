package com.jerry.up.lala.framework.core.redis;

import com.jerry.up.lala.framework.core.common.CommonConstant;
import com.jerry.up.lala.framework.core.common.DataBody;
import com.jerry.up.lala.framework.core.exception.ServiceException;
import com.jerry.up.lala.framework.core.data.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: redis log 组件 方便快速记录日志
 *
 * @author FMJ
 * @date 2023/11/3 09:44
 */
@Component
public class RedisLogComponent<V> {

    @Autowired
    private RedisListTemplate<V> redisListTemplate;

    public String key(DataBody<String> dataBody) {
        String id = CheckUtil.dataNull(dataBody);
        return CommonConstant.REDIS_KEY_LOG + id;
    }

    public void add(RedisLogAddBO<V> redisLogBO) {
        try {
            redisListTemplate.addTail(key(redisLogBO), redisLogBO.getInfo());
        } catch (Exception e) {
            throw ServiceException.error(e);
        }
    }

    public RedisLogInfoBO<V> get(DataBody<String> dataBody) {
        try {
            String key = key(dataBody);
            List<V> infos = redisListTemplate.get(key);
            return new RedisLogInfoBO<V>().setKey(key).setInfos(infos);
        } catch (Exception e) {
            throw ServiceException.error(e);
        }
    }

}
